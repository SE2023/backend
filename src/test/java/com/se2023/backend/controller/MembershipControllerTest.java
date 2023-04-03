package com.se2023.backend.controller;

import com.se2023.backend.entity.User;
import com.se2023.backend.utils.JsonResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MembershipControllerTest {

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
    void setMembership(){
        String url = urlPrefix + "/membership";

        User user = new User();

        JsonResult result;

        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(500, result.getCode());
        assertEquals("Something missing!", result.getMessage());
        assertEquals("fail", result.getType());

        user.setId(1);
        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(500, result.getCode());
        assertEquals("You are already a membership", result.getMessage());
        assertEquals("fail", result.getType());

        //每次实验前需要先清空此调数据库信息
        user.setId(2);
        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Successfully join in the membership!", result.getMessage());
        assertEquals("success", result.getType());
    }


    @Test
    void removeMembership(){
        String url = urlPrefix + "/membership/{id}";

        User user = new User();

        JsonResult result;

        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(500, result.getCode());
        assertEquals("Missing user id!", result.getMessage());
        assertEquals("fail", result.getType());

        user.setId(0);
        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(500, result.getCode());
        assertEquals("Invalid membership!", result.getMessage());
        assertEquals("fail", result.getType());

        user.setId(2);
        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Successfully remove this membership!", result.getMessage());
        assertEquals("success", result.getType());
    }

    @Test
    void queryAllMembership(){
        String url = urlPrefix + "/membership";

        JsonResult result = restTemplate.getForObject(url, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Successfully query all membership", result.getMessage());
        assertEquals("success", result.getType());
    }
}
