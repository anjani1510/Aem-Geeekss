package com.aem.geeks.core.models;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderSling {
    
    @ValueMapValue
    private String pathField;
    
    @ValueMapValue
    private String textField;
    
    @ValueMapValue
    private String checkbox;
    
    @ChildResource
    private List<Multifield> multifield;

    public List<Multifield> getMultiField() {
        return multifield;
    }

    public String getPathField() {
        return pathField;
    }

    public String getTextField() {
        return textField;
    }

    public String getCheckBox() {
        return checkbox;
    }
}