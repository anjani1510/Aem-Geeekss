package com.aem.geeks.core.config;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.AttributeDefinition;

@ObjectClassDefinition(name = "My Scheduler Configuration")
public @interface MySchedulerConfiguration {

    @AttributeDefinition(
        name = "Cron Expression",
        description = "Cron expression for scheduler"
    )
    String cronExpression() default "0 0 12 * * ?";
}
