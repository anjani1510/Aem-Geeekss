package com.aem.geeks.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactModel {

    private static final Logger log = LoggerFactory.getLogger(ContactModel.class);

    @Self
    private Resource resource;

    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        ResourceResolver resolver = resource.getResourceResolver();
        Resource contactRootResource = resolver.getResource("/content/campaigns");

        if (contactRootResource != null) {
            for (Resource res : contactRootResource.getChildren()) {
                // Check if the resource is of the correct type (assuming nt:unstructured or similar)
                if (res.isResourceType("nt:unstructured")) {
                    log.info("Processing contact: " + res.getPath());
                    Contact contact = new Contact();
                    contact.setName(res.getValueMap().get("name", String.class));
                    contact.setEmail(res.getValueMap().get("email", String.class));
                    contact.setMobile(res.getValueMap().get("mobile", String.class));
                    contacts.add(contact);
                } else {
                    log.warn("Skipping resource at " + res.getPath() + " due to incorrect type.");
                }
            }
        } else {
            log.warn("No contact resource found at /content/campaigns");
        }
        return contacts;
    }

    public class Contact {
        private String name;
        private String email;
        private String mobile;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}