package com.aem.geeks.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name="ArticleSchedulerConfigurations")
public @interface ArticleSchedulerConfigurations {

      @AttributeDefinition(name="SchedulerExpression")
      String SchedulerExpression() default "*/0 * * * * ?";

      @AttributeDefinition(name="concurrent")
      boolean concurrent() default false;

      @AttributeDefinition(name="SchedulerExpression")
      String SchedulerName() default "Article";

      @AttributeDefinition(name="need to enable scheduler")
      boolean enable() default true;


}
