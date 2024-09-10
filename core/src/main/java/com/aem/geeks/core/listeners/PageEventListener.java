package com.aem.geeks.core.listeners;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component(
    service = {EventHandler.class, ResourceChangeListener.class},
    immediate = true,
    property = {
        ResourceChangeListener.PATHS + "=/content", 
        ResourceChangeListener.CHANGES + "=ADDED",
        ResourceChangeListener.CHANGES + "=REMOVED"
    }
)
public class PageEventListener implements ResourceChangeListener, EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PageEventListener.class);

    @Override
    public void onChange(List<ResourceChange> changes) {
        for (ResourceChange change : changes) {
            String changeType = change.getType().toString();
            String resourcePath = change.getPath();
            LOG.info("Change detected: Type={}, Path={}", changeType, resourcePath);

            if ("ADDED".equals(changeType)) {
                LOG.info("Page published: {}", resourcePath);
            } else if ("REMOVED".equals(changeType)) {
                LOG.info("Page unpublished: {}", resourcePath);
            }
        }
    }

    @Override
    public void handleEvent(Event event) {
        String eventTopic = event.getTopic();
        String resourcePath = (String) event.getProperty("path");

        LOG.info("Event received: Topic={}, Path={}", eventTopic, resourcePath);

        if (eventTopic.equals("org/apache/sling/api/resource/Resource/ADDED")) {
            LOG.info("Page published: {}", resourcePath);
        } else if (eventTopic.equals("org/apache/sling/api/resource/Resource/REMOVED")) {
            LOG.info("Page unpublished: {}", resourcePath);
        }
    }
}
