package com.aem.geeks.core.listeners;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.model.WorkflowModel;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component(
    service = EventHandler.class,
    property = {
        "event.topics=org/apache/sling/api/resource/Resource/ADDED"
    }
)
public class Task10 implements EventHandler {

    private static final Logger log = LoggerFactory.getLogger(Task10.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    private static final String PROJECT_TEMPLATE_PATH = "/conf/project2/settings/wcm/templates/";

    @Override
    public void handleEvent(Event event) {
        String path = (String) event.getProperty("path");
        log.info("Handling event for path: {}", path);

        if (path != null && path.startsWith("/content") && path.endsWith("/jcr:content")) {
            Map<String, Object> params = new HashMap<>();
            params.put(ResourceResolverFactory.SUBSERVICE, "anjani");

            try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(params)) {
                log.info("ResourceResolver obtained for service user 'anjani'");

                // Get the page resource
                Resource pageResource = resolver.getResource(path);
                if (pageResource != null) {
                    log.info("Page resource found: {}", path);
                    String templatePath = pageResource.getValueMap().get("cq:template", String.class);
                    log.info("Template path: {}", templatePath);

                    Calendar expiryDate = Calendar.getInstance();

                    // Check the template path and set the expiry date accordingly
                    if (templatePath != null && templatePath.startsWith(PROJECT_TEMPLATE_PATH)) {
                        // Set expiry date to the current date
                        expiryDate.setTimeInMillis(System.currentTimeMillis());
                        log.info("Setting expiry date to current date");
                    } else {
                        // Set expiry date to the previous date (1 day before)
                        expiryDate.add(Calendar.DAY_OF_MONTH, -1);
                        log.info("Setting expiry date to previous date");
                    }

                    // Set the expiry date property as a Calendar (Date) object
                    ModifiableValueMap properties = pageResource.adaptTo(ModifiableValueMap.class);
                    if (properties != null) {
                        properties.put("expiryDate", expiryDate);
                        resolver.commit();
                        log.info("Expiry date property set to: {}", expiryDate.getTime());
                    }

                    // Trigger the workflow
                    startWorkflow(resolver, path);
                } else {
                    log.warn("Page resource not found: {}", path);
                }
            } catch (LoginException | PersistenceException | WorkflowException e) {
                log.error("Error handling page creation event", e);
            }
        }
    }

    private void startWorkflow(ResourceResolver resolver, String path) throws WorkflowException {
        WorkflowSession wfSession = resolver.adaptTo(WorkflowSession.class);
        if (wfSession != null) {
            log.info("Starting workflow for path: {}", path);
            WorkflowModel wfModel = wfSession.getModel("/var/workflow/models/add-expiry-date");
            if (wfModel != null) {
                WorkflowData wfData = wfSession.newWorkflowData("JCR_PATH", path);
                wfSession.startWorkflow(wfModel, wfData);
                log.info("Workflow started for page: {}", path);
            } else {
                log.warn("Workflow model not found at path: /var/workflow/models/add-expiry-date");
            }
        } else {
            log.warn("Workflow session not available");
        }
    }
}
