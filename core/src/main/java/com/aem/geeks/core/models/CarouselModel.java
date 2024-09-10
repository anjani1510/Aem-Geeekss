package com.aem.geeks.core.models;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CarouselModel {
   @ChildResource
    private List<CarouselItem> carouselItems;
    public List<CarouselItem> getCarouselItems() {
        return carouselItems;
    }

    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class CarouselItem {

        @ValueMapValue
        private String text;

        @ValueMapValue
        private String path;

        @ChildResource
        private List<NestedItem> nestedItems;

        public String getText() {
            return text;
        }

        public String getPath() {
            return path;
        }

        public List<NestedItem> getNestedItems() {
            return nestedItems;
        }

        @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
        public static class NestedItem {

            @ValueMapValue
            private String nestedText;

            public String getNestedText() {
                return nestedText;
            }
        }
    }
}
