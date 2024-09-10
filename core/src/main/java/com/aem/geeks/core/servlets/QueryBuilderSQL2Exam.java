package com.aem.geeks.core.servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.jcr.query.Query;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import com.day.cq.wcm.api.Page;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/sql2/examp")
public class QueryBuilderSQL2Exam extends SlingSafeMethodsServlet {


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        //String query = "SELECT * FROM [cq:Page] AS s WHERE ISDESCENDANTNODE([/content/aemgeeks]) AND s.[jcr:content/jcr:title] like'%e%'";
        String queryparam = request.getParameter("qparam");
        String query = "SELECT * FROM [cq:Page] AS s WHERE ISDESCENDANTNODE([/content/aemgeeks]) AND s.[jcr:content/jcr:title] like '%"+ queryparam + "%'";
        ResourceResolver resolver = request.getResourceResolver();
        Iterator<Resource> results = resolver.findResources(query, Query.JCR_SQL2);
        JsonArrayBuilder jab = Json.createArrayBuilder();
        while (results.hasNext()) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            Resource resource = results.next();
            Page currentPage = resource.adaptTo(Page.class);
            if (currentPage != null) {
                job.add("title", currentPage.getTitle());
                job.add("path", currentPage.getPath());
                jab.add(job);
            }

        }
        response.getWriter().write(jab.build().toString());
    }

}