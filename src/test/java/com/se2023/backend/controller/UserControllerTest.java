package com.se2023.backend.controller;

import com.se2023.backend.entity.User;
import com.se2023.backend.utils.JsonResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

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
    void Login() {
        String url = urlPrefix + "/login";

        // invalid username
        User user = new User();
        user.setUsername("invalid_username");
        user.setPassword("123456");
        JsonResult result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(400, result.getCode());
        assertEquals("Invalid username", result.getMessage());
        assertEquals("failed", result.getType());

        // invalid password
        user.setUsername("Manager1");
        user.setPassword("invalid_password");
        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(400, result.getCode());
        assertEquals("Invalid password", result.getMessage());
        assertEquals("failed", result.getType());

        // valid username and password
        user.setUsername("Manager1");
        user.setPassword("123456");
        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Manager login", result.getMessage());
        assertEquals("success", result.getType());

    }

    @Test
    void consumer_email() {
    }

    @Test
    void c_register() {
    }

    @Test
    void staff_email() {
    }

    @Test
    void s_register() {
    }

    @Test
    void queryUser() {
    }

    @Test
    void getUserInfo() {
    }

    @Test
    void queryAllNonmembers() {
    }

    @Test
    void queryAllStaffs() {
    }

    @Test
    void queryAllMembers() {
    }
}