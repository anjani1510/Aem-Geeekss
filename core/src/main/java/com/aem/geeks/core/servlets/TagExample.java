package com.aem.geeks.core.servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.json.JsonObject;
import javax.servlet.Servlet;
import javax.servlet.ServletException;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

@Component(service = Servlet.class,immediate =true)
@SlingServletPaths(value="/apps/tagexample")

public class TagExample extends SlingSafeMethodsServlet{

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
                ResourceResolver resolver = request.getResourceResolver();
               Tag resolve= resolver.adaptTo(TagManager.class).resolve("/content/cq:tags/mainwebsite/color");
               JSONObject pagesObject=new JSONObject();
               Iterator<Tag> listChildren=resolve.listChildren();
               JSONArray pagesArray=new JSONArray();
                while(listChildren.hasNext())
                {
                    Tag next=listChildren.next();
                   try{
                    pagesObject.put(next.getTitle(), next.getPath().toString());
                    pagesArray.put(pagesObject);
                   }
                catch(JSONException e){
                    e.printStackTrace();
                }
            
                }

                response.getWriter().write(pagesArray.toString());
    }
    

}
