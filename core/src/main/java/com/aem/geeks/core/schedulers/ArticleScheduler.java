package com.aem.geeks.core.schedulers;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Runnable.class,immediate = true, name = "ArticleScheduler" )
@Designate(ocd=ArticleSchedulerConfigurations.class)
public class ArticleScheduler implements Runnable {

    private static final Logger LOG= LoggerFactory.getLogger(ArticleScheduler.class);
    @Reference
    Scheduler scheduler;

    @Override
    public void run() {
        
        LOG.info("ArticleScheduler run() execution started");
    }

    @Activate
    public void activate(ArticleSchedulerConfigurations configurations){
        updateConfigurations(configurations);
    }

    @Deactivate
    public void deactivate(ArticleSchedulerConfigurations configurations){
        updateConfigurations(configurations);
    }
    
    @Modified
    public void modified(ArticleSchedulerConfigurations configurations)
    {
        updateConfigurations(configurations);
    }

    
    public void updateConfigurations(ArticleSchedulerConfigurations configurations)
    {
     if(configurations.enable())
     {
        ScheduleOptions options= scheduler.EXPR(configurations.SchedulerExpression() );
     options.canRunConcurrently(false);
     options.name(configurations.SchedulerName());
     scheduler.schedule(this, options);
     LOG.info("update the status here{}", configurations.SchedulerExpression());
     }
     else{
        scheduler.unschedule(configurations.SchedulerName());
     }
    }
    

}
