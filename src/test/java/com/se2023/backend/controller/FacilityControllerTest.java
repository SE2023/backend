package com.se2023.backend.controller;

import com.se2023.backend.entity.Activity.Facility;
import com.se2023.backend.utils.JsonResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class FacilityControllerTest {
    private final RestTemplate restTemplate = new RestTemplate();

    private final String urlPrefix = "http://localhost:8880";

    @BeforeEach
    void setUp() {
        System.out.println("setUp");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearDown");
    }

    @Test
    void addFacility() {
        String url = urlPrefix + "/facility";

        JsonResult result = restTemplate.postForObject(url, new Facility(), JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Add facility", result.getMessage());
        assertEquals("success", result.getType());
    }

    @Test
    void getFacility() {
        String url = urlPrefix + "/facility";

        JsonResult result = restTemplate.getForObject(url, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Get facility", result.getMessage());
        assertEquals("success", result.getType());
    }

    @Test
    void getFacilityById() {
        String url = urlPrefix + "/facility/1";

        JsonResult result = restTemplate.getForObject(url, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Get facility by id", result.getMessage());
        assertEquals("success", result.getType());
    }
}