package com.aem.geeks.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleDemoImp implements ArticleDemo {
@ValueMapValue
   private String text;
@ValueMapValue
    private String description;
@ValueMapValue
    private int dob;
@ValueMapValue
    private String image;
     @Override
    public int getDate() {
        
        return dob;
    }

    @Override
    public String getDescription() {
       
        return description;
    }

    @Override
    public String getImage() {
        
        return image;
    }

    @Override
    public String getText() {
        
        return text;
    }
    

}
