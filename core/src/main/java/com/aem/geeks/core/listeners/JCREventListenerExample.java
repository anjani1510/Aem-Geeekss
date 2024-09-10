package com.aem.geeks.core.listeners;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import org.apache.commons.logging.Log;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component(immediate=true,service=EventListener.class)
public class JCREventListenerExample implements EventListener {
     public static final Logger LOG=LoggerFactory.getLogger(JCREventListenerExample.class);
     private Session session;
     @Reference
     SlingRepository slingRepository;
     @Activate
     public void activate() throws Exception{
        try{

            
            session=slingRepository.loginService("", null);
            session.getWorkspace().getObservationManager().addEventListener(this,
            Event.NODE_ADDED | Event.PROPERTY_ADDED,
            "/content/aemgeeks/us/en",
            true,
            null,
            null,
            true);
        }
        catch(RepositoryException e){
            LOG.info("\n Error while adding Event Listener : {}", e.getMessage());
        }
     }
     @Override
     public void onEvent(EventIterator events) {
         try {
             while (events.hasNext()) {
                 Event event = events.nextEvent();
                 LOG.info("\n path : {}", event.getPath());
             }
         } catch (RepositoryException e) {
             LOG.error("\n Error processing event : {}", e.getMessage());
         }
     }
 }
