package com.se2023.backend.controller;

import com.se2023.backend.utils.JsonResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class SportsCentreControllerTest {
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
    void getSportsCentreById() {
        String url = urlPrefix + "/sports_centre/1";

        JsonResult result = restTemplate.getForObject(url, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Get sports centre by id", result.getMessage());
        assertEquals("success", result.getType());
    }
}