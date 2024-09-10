package com.aem.geeks.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;

import org.apache.commons.lang.StringUtils;

@Component(service=Servlet.class)
@SlingServletPaths(value = "/bin/workflow/examp")
public class WorkflowExamp extends SlingSafeMethodsServlet {
  private static final Logger LOG=LoggerFactory.getLogger(WorkflowExamp.class);
  public String workflow="not execcuting";
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
    
       ResourceResolver resourceResolver=request.getResourceResolver();
       String payload=request.getParameter("page").toString();
       try{
       if(StringUtils.isNotBlank(payload))
       {
        WorkflowSession ws=  resourceResolver.adaptTo(WorkflowSession.class);
       WorkflowModel wm= ws.getModel("/var/workflow/models/page-version-model");
        WorkflowData wd=ws.newWorkflowData("JCR_PATH",payload);
          workflow=ws.startWorkflow(wm,wd).getState() ;  
          }
    }
    catch(Exception e)
    {
         LOG.info("showing workflow exception or error{}",e.getMessage());
    }
    response.getWriter().write(workflow);
    }
    

}
