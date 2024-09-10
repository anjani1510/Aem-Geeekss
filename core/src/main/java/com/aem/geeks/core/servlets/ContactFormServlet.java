package com.aem.geeks.core.servlets;


import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class,
           property = {
               "sling.servlet.methods=POST,GET,DELETE",
               "sling.servlet.paths=/bin/contactFormServlet"
           })
@ServiceDescription("Contact Form Servlet")
public class ContactFormServlet extends SlingAllMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ContactFormServlet.class);

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try {
            String payload = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
            JSONObject json = new JSONObject(payload);

            String name = json.getString("name");
            String email = json.getString("email");
            String mobile = json.getString("mobile");

            Session session = request.getResourceResolver().adaptTo(Session.class);
            Node rootNode = session.getRootNode();
            Node catalogsNode = rootNode.getNode("content/catalogs");

            Node newContact = catalogsNode.addNode(name, JcrConstants.NT_UNSTRUCTURED);
            newContact.setProperty("name", name);
            newContact.setProperty("email", email);
            newContact.setProperty("mobile", mobile);

            session.save();
            response.getWriter().write("Contact saved successfully!");

        } catch (Exception e) {
            LOG.error("Error while saving contact data", e);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try {
            String name = request.getParameter("name");

            Session session = request.getResourceResolver().adaptTo(Session.class);
            Node catalogsNode = session.getRootNode().getNode("content/campaigns");
            Node contactNode = catalogsNode.getNode(name);

            JSONObject contact = new JSONObject();
            contact.put("name", contactNode.getProperty("name").getString());
            contact.put("email", contactNode.getProperty("email").getString());
            contact.put("mobile", contactNode.getProperty("mobile").getString());

            response.getWriter().write(contact.toString());

        } catch (Exception e) {
            LOG.error("Error while fetching contact data", e);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try {
            String name = request.getParameter("name");

            Session session = request.getResourceResolver().adaptTo(Session.class);
            Node catalogsNode = session.getRootNode().getNode("content/campaigns");
            Node contactNode = catalogsNode.getNode(name);
            contactNode.remove();

            session.save();
            response.getWriter().write("Contact deleted successfully!");

        } catch (Exception e) {
            LOG.error("Error while deleting contact", e);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}