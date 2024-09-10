package com.aem.geeks.core.config;




import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Page Expiry Scheduler Configuration")
public @interface SchedulerConfig {

    @AttributeDefinition(
        name = "Scheduler expression",
        description = "Cron expression to trigger the scheduler"
    )
    String scheduler_expression() default "*/3 * * * * ?"; // Default to 3 minutes

    @AttributeDefinition(
        name = "Concurrent",
        description = "Whether the scheduler should run concurrently"
    )
    boolean scheduler_concurrent() default false;
}
