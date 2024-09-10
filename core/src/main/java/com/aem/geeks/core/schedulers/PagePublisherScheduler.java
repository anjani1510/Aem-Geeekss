package com.aem.geeks.core.schedulers;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import com.day.cq.replication.ReplicationException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.api.resource.LoginException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.framework.Constants;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.Map;

@Component(service = Runnable.class, immediate = true,
    property = {
        Constants.SERVICE_DESCRIPTION + "=Page Publisher Scheduler",
        "scheduler.concurrent=false"
    })
@Designate(ocd = PagePublisherScheduler.Config.class)
public class PagePublisherScheduler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(PagePublisherScheduler.class);

    @Reference
    private Scheduler scheduler;

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private Replicator replicator;

    private String pagePath;
    private ScheduleOptions scheduleOptions;
    private boolean enabled;

    @ObjectClassDefinition(name="Page Publisher Scheduler Configuration")
    public @interface Config {

        @AttributeDefinition(name="Cron Expression for Scheduler", description = "Cron expression for scheduling the task")
        String schedulerExpression() default "0 0 12 1/1 * ? *"; // Default: run every day at noon

        @AttributeDefinition(name="Enable Scheduler", description = "Enable or disable the scheduler")
        boolean enable() default true;

        @AttributeDefinition(name="Page Path", description = "Path of the page to be processed")
        String pagePath() default "/content/aemgeeks";
    }

    @Activate
    @Modified
    protected void activate(Config config) {
        this.pagePath = config.pagePath();
        String expression = config.schedulerExpression();
        this.enabled = config.enable();

        if (enabled) {
            scheduleOptions = scheduler.EXPR(expression).name(PagePublisherScheduler.class.getName());
            scheduler.schedule(this, scheduleOptions);
            LOG.info("Scheduler activated with expression: {}", expression);
        } else {
            LOG.info("Scheduler is disabled.");
        }
    }

    @Deactivate
    protected void deactivate() {
        if (enabled) {
            scheduler.unschedule(PagePublisherScheduler.class.getName());
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        if (enabled) {
            try (ResourceResolver resourceResolver = resolverFactory.getServiceResourceResolver(Map.of(ResourceResolverFactory.SUBSERVICE, "anjani"))) {
                Resource rootResource = resourceResolver.getResource(pagePath);
                if (rootResource != null) {
                    for (Resource child : rootResource.getChildren()) {
                        if (ResourceUtil.isA(child, "cq:Page")) {
                            replicatePage(resourceResolver, child.getPath());
                        }
                    }
                }
            } catch (LoginException e) {
                LOG.error("Error obtaining resource resolver", e);
            }
        } else {
            LOG.info("Scheduler is disabled.");
        }
    }

    private void replicatePage(ResourceResolver resourceResolver, String pagePath) {
        try {
            replicator.replicate(resourceResolver.adaptTo(Session.class), ReplicationActionType.ACTIVATE, pagePath);
            LOG.info("Published page: {}", pagePath);
        } catch (ReplicationException e) {
            LOG.error("Failed to replicate page: {}", pagePath, e);
        }
    }
}