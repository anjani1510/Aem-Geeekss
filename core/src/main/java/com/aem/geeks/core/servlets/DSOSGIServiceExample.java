package com.aem.geeks.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
//import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
@Component(service=Servlet.class)
//@SlingServletResourceTypes(resourceTypes ={"aemgeeks/components/page"})
@SlingServletPaths(value="/bin/demo/recent")
public class DSOSGIServiceExample extends SlingSafeMethodsServlet{
  @Reference
  QueryBuilder queryBuilder;
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        Map<String,String> predicate =new HashMap<String,String>();
        predicate.put("type", "cq:Page");
        predicate.put("path", "/content/aemgeeks/us/en");
        predicate.put("oredrby" ,"@jcr:content/cq:lastModified");
        predicate.put("p.limit", "-1");
        

      Query query=queryBuilder.createQuery(PredicateGroup.create(predicate), request.getResourceResolver().adaptTo(Session.class));
       SearchResult result=query.getResult();
       List<Hit> resourcList=result.getHits();
       JsonArrayBuilder jab = Json.createArrayBuilder();
       for(Hit hit:resourcList)
       {
       
       try{
        JsonObjectBuilder job= Json.createObjectBuilder();
       Resource resource=hit.getResource();
       Resource content = resource.getResourceResolver().getResource(resource.getPath() + "/jcr:content");
                job.add("title", content.getValueMap().get("jcr:title", String.class));
                job.add("path", resource.getPath());
                jab.add(job);
            } catch (RepositoryException e) {
                
                e.printStackTrace();
            }
        }
        response.getWriter().write(jab.build().toString());
}
    }


