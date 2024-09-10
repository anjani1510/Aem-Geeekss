package com.aem.geeks.core.listeners;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = EventHandler.class,
    property = {
        "event.topics=com/day/cq/replication/job/activate",
        "event.topics=com/day/cq/replication/job/deactivate"
    }
)
public class PageReplicationEventListener implements EventHandler {

    private static final Logger log = LoggerFactory.getLogger(PageReplicationEventListener.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public void handleEvent(Event event) {
        String topic = event.getTopic();
        String path = (String) event.getProperty("path");

        if (path != null) {
            if (topic.equals("com/day/cq/replication/job/activate")) {
                log.info("Page published: {}", path);
            } else if (topic.equals("com/day/cq/replication/job/deactivate")) {
                log.info("Page unpublished: {}", path);
            } else {
                log.info("Other replication event: {}", topic);
            }
        } else {
            log.info("Event with no path: {}", event);
        }
    }
}
