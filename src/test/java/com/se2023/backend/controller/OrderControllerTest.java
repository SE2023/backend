package com.se2023.backend.controller;

import com.se2023.backend.entity.Order.Order;
import com.se2023.backend.utils.JsonResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {
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
    void getOrder() {
        String url = urlPrefix + "/order";

        JsonResult result = restTemplate.getForObject(url, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Get order", result.getMessage());
        assertEquals("success", result.getType());
    }

    @Test
    void getOrderById() {
        String url = urlPrefix + "/order/1";

        JsonResult result = restTemplate.getForObject(url, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Get order by id", result.getMessage());
        assertEquals("success", result.getType());
    }

    @Test
    void addOrder() {
        String url = urlPrefix + "/order";

        JsonResult result = restTemplate.postForObject(url, new Order(), JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Add order", result.getMessage());
        assertEquals("success", result.getType());
    }
}