package com.aem.geeks.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
public class HeaderSlingTest {

    private final AemContext aemContext = new AemContext();
    private HeaderSling headerSling;

    @BeforeEach
    void setUp() {
        aemContext.addModelsForClasses(HeaderSling.class, Multifield.class);

        // Define properties for HeaderSling model
        Map<String, Object> headerProps = new HashMap<>();
        headerProps.put("pathField", "/apps/Demo");
        headerProps.put("textField", "first textfield");
        headerProps.put("checkbox", "on");

        // Create the main resource
        Resource headerResource = aemContext.create().resource("/content/header", headerProps);

        // Define properties for Multifield
        Map<String, Object> multifieldProps = new HashMap<>();
        multifieldProps.put("datePicker", "2024-07-26T00:00:00.000+05:30");
        multifieldProps.put("multiText", "second text field");

        // Create multifield resource under the main resource
        aemContext.create().resource("/content/header/multifield/0", multifieldProps);

        // Adapt the resource to HeaderSling model
        headerSling = headerResource.adaptTo(HeaderSling.class);
    }

    @Test
    void getPathField() {
        assertEquals("/apps/Demo", headerSling.getPathField(), "Path field value does not match");
    }

    @Test
    void getTextField() {
        assertEquals("first textfield", headerSling.getTextField(), "Text field value does not match");
    }

    @Test
    void getCheckBox() {
        assertEquals("on", headerSling.getCheckBox(), "Checkbox value does not match");
    }

    @Test
    void getMultiField() {
        List<Multifield> multifields = headerSling.getMultiField();
        assertNotNull(multifields, "Multifield list should not be null");
        assertEquals(1, multifields.size(), "Multifield size does not match");

        // Test first multifield item
        Multifield multifield = multifields.get(0);
        assertEquals("2024-07-26T00:00:00.000+05:30", multifield.getDatePicker(), "Date picker value does not match");
        assertEquals("second text field", multifield.getMultiText(), "Multi text value does not match");
    }
}
