package com.aem.geeks.core.listeners;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component(service=EventHandler.class,immediate = true,property={
    EventConstants.EVENT_TOPIC+"=org/apache/sling/api/resource/Resource/ADDED",
    EventConstants.EVENT_TOPIC+"=org/apache/sling/api/resource/Resource/REMOVED",
    EventConstants.EVENT_TOPIC+"=org/apache/sling/api/resource/Resource/CHANGED",
    EventConstants.EVENT_TOPIC+"=com/day/cq/replication",
    EventConstants.EVENT_FILTER+"=(|(|(path=/content/aemgeeks/us/en/*)(path=/content/aemgeeks/us/*))(type=activate))"
})
public class EventHandlerExample implements EventHandler {
 public static final Logger LOG=LoggerFactory.getLogger(EventHandlerExample.class);
    @Override
    public void handleEvent(Event event) {
        LOG.info("Event Handler is executed");
        LOG.info("Topic name{}",event.getTopic());
        String[] propertyNames=event.getPropertyNames();
        for(String name:propertyNames)
        {
            LOG.info("Property name{},Property Value{}",name,event.getProperty(name).toString());

        }
       
        
    }
    

}
