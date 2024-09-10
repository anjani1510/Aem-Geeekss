package com.aem.geeks.core.models;

import java.sql.Date;

import javax.annotation.Resource;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class VisionTechImpl implements VisionTech {

    @ScriptVariable
    private Page currentPage;

    @ValueMapValue
    private String articleDes;

    @ValueMapValue
    private Date articleDate;

    @ValueMapValue
    private String articleImage;

    @ValueMapValue
    private String articleTitle;

    @ValueMapValue(name = "sling:resourceType")
    private String slingResourceType;

    @Override
    public Date getExpirydate() {
       
        return articleDate;
    }

    @Override
    public String getVisionDescription() {
       
        return articleDes;
    }

    @Override
    public String getVisionImage() {
       
        return articleImage;
    }

    @Override
    public String getVisionTitle() {
        
        return articleTitle ;
    }

    @Override
    public String pageTitle() {
        
        return currentPage.getPageTitle();
    }
    




 

}
