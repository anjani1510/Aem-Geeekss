package com.aem.geeks.core.models;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
public class VisionTechImplTest {
    private final AemContext aemContext = new AemContext();
    private VisionTechImpl vision;
    @BeforeEach
    void setUp(){
        aemContext.addModelsForClasses(VisionTechImpl.class);
         Map<String, Object> vis = new HashMap<>();
         vis.put("articleDes", "hello world");

    }
    @Test
    void testGetExpirydate() {

    }

    @Test
    void testGetVisionDescription() {

    }

    @Test
    void testGetVisionImage() {

    }

    @Test
    void testGetVisionTitle() {

    }

    @Test
    void testPageTitle() {

    }
}
