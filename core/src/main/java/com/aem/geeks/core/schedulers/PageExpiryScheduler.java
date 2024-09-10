package com.aem.geeks.core.schedulers;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Collections;

import javax.jcr.Session;


@Component(
    service = Runnable.class,
    property = {
        "scheduler.expression=*/3 * * * * ?", // Every 3 minutes
        "scheduler.concurrent=false"
    }
)
@Designate(ocd = SchedulerConfig.class)
public class PageExpiryScheduler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(PageExpiryScheduler.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private Replicator replicator;

    @Override
    public void run() {
        try (ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(
                Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "anjani"))) {

            Resource rootResource = resourceResolver.getResource("/content");
            if (rootResource != null) {
                for (Resource page : rootResource.getChildren()) {
                    Resource content = page.getChild("jcr:content");
                    if (content != null) {
                        ValueMap properties = content.getValueMap();
                        Calendar expiryDate = properties.get("expiryDate", Calendar.class);

                        if (expiryDate != null) {
                            Calendar currentDate = Calendar.getInstance();

                            if (isSameDay(currentDate, expiryDate)) {
                                // Publish page
                                replicator.replicate((Session) resourceResolver.adaptTo(com.day.cq.replication.Replicator.class),
                                        ReplicationActionType.ACTIVATE, page.getPath());
                                log.info("Published page: {}", page.getPath());
                            } else if (expiryDate.before(currentDate)) {
                                // Unpublish page
                                replicator.replicate((Session) resourceResolver.adaptTo(com.day.cq.replication.Replicator.class),
                                        ReplicationActionType.DEACTIVATE, page.getPath());
                                log.info("Unpublished page: {}", page.getPath());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error in scheduler", e);
        }
    }

    private boolean isSameDay(Calendar date1, Calendar date2) {
        return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
               date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR);
    }
}
