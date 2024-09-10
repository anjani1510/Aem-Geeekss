package com.aem.geeks.core.workflows;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Collections;

@Component(
    service = WorkflowProcess.class,
    property = {"process.label=Add Expiry Date Process"}
)
public class AddExpiryDateProcess implements WorkflowProcess {

    private static final Logger log = LoggerFactory.getLogger(AddExpiryDateProcess.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private static final String PROJECT_TEMPLATE_PATH = "/conf/project2/settings/wcm/templates/";

    @Override
    public void execute(WorkItem workItem, com.day.cq.workflow.WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        String payloadPath = workItem.getWorkflowData().getPayload().toString();
        try (ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(
                Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "anjani"))) {

            Resource pageResource = resourceResolver.getResource(payloadPath + "/jcr:content");
            if (pageResource != null) {
                ModifiableValueMap properties = pageResource.adaptTo(ModifiableValueMap.class);
                String templatePath = properties.get("cq:template", String.class);

                Calendar calendar = Calendar.getInstance();

                if (templatePath != null && templatePath.startsWith(PROJECT_TEMPLATE_PATH)) {
                    // Set expiryDate to the current date for the project's templates
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    log.info("Setting expiry date to current date for template: {}", templatePath);
                } else {
                    // Set expiryDate to the previous date for other templates
                    calendar.add(Calendar.DATE, -1);
                    log.info("Setting expiry date to previous date for template: {}", templatePath);
                }

                // Set the expiryDate property as a Calendar (Date) object
                properties.put("expiryDate", calendar);
                resourceResolver.commit();
                log.info("Expiry date property set for page: {} with date: {}", payloadPath, calendar.getTime());
            } else {
                log.warn("Page resource not found at path: {}", payloadPath + "/jcr:content");
            }
        } catch (Exception e) {
            log.error("Error adding expiry date to page at path: {}", payloadPath, e);
        }
    }
}
