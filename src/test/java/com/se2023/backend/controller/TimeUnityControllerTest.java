package com.se2023.backend.controller;

import com.se2023.backend.entity.Others.TimeUnity;
import com.se2023.backend.utils.JsonResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.sql.Time;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TimeUnityControllerTest {
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
    void addTimeUnity() {
        String url = urlPrefix + "/timeUnity";

        TimeUnity timeUnity = new TimeUnity();
        timeUnity.setDate(new Date());
        timeUnity.setStartTime(new Time(new Date().getTime()));
        timeUnity.setEndTime(new Time(new Date().getTime()));

        JsonResult result = restTemplate.postForObject(url, timeUnity, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Add time unity", result.getMessage());
        assertEquals("success", result.getType());
    }
}