package com.se2023.backend.controller;

import com.se2023.backend.entity.User;
import com.se2023.backend.utils.JsonResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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
        String url_0 = urlPrefix + "/membership/set/1";
        Map<String,Double> obj_0=new HashMap<>();
        JsonResult result_0;
        result_0 = restTemplate.postForObject(url_0, obj_0, JsonResult.class);
        assertEquals(400, result_0.getCode());
        assertEquals("Balance can not under 100 dollars", result_0.getMessage());
        assertEquals("failed", result_0.getType());

        String url0 = urlPrefix + "/membership/set/5";
        Map<String,Double> obj0=new HashMap<>();
        JsonResult result0;
        result0 = restTemplate.postForObject(url0, obj0, JsonResult.class);
        assertEquals(400, result0.getCode());
        assertEquals("Balance can not under 100 dollars", result0.getMessage());
        assertEquals("failed", result0.getType());

        String url = urlPrefix + "/membership/set/5";
        Map<String,Double> obj=new HashMap<>();
        obj.put("balance",40.0);
        JsonResult result;
        result = restTemplate.postForObject(url, obj, JsonResult.class);
        assertEquals(400, result.getCode());
        assertEquals("Balance can not under 100 dollars", result.getMessage());
        assertEquals("failed", result.getType());

        String url1 = urlPrefix + "/membership/set/5";
        Map<String,Double> obj1=new HashMap<>();
        obj1.put("balance",400.0);
        JsonResult result1;
        result1 = restTemplate.postForObject(url1, obj1, JsonResult.class);
        assertEquals(0, result1.getCode());
        assertEquals("Successfully join in the membership!", result1.getMessage());
        assertEquals("success", result1.getType());

        String url2 = urlPrefix + "/membership/set/5";
        obj.put("balance",400.0);
        JsonResult result2;
        result2 = restTemplate.postForObject(url2, obj, JsonResult.class);
        assertEquals(400, result2.getCode());
        assertEquals("You are already a membership", result2.getMessage());
        assertEquals("failed", result2.getType());

    }

    @Test
    void removeMembership(){
        String url = urlPrefix + "/membership/remove/5656";
        Map<String,Double> obj=new HashMap<>();
        JsonResult result;
        result = restTemplate.postForObject(url, obj, JsonResult.class);
        assertEquals(400, result.getCode());
        assertEquals("Invalid user id.", result.getMessage());
        assertEquals("failed", result.getType());

        String url1 = urlPrefix + "/membership/remove/5";
        Map<String,Double> obj1=new HashMap<>();
        JsonResult result1;
        result1 = restTemplate.postForObject(url1, obj1, JsonResult.class);
        assertEquals(0, result1.getCode());
        assertEquals("Successfully remove this membership!", result1.getMessage());
        assertEquals("success", result1.getType());
    }


    @Test
    void queryAllMembership(){
        String url = urlPrefix + "/membership";

        JsonResult result = restTemplate.getForObject(url, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Successfully query all membership", result.getMessage());
        assertEquals("success", result.getType());
    }

    @Test
    void queryMembership(){
        String url = urlPrefix + "/membership/1";
        Map<String,Double> obj=new HashMap<>();
        JsonResult result = restTemplate.postForObject(url, obj,JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Successfully get membership", result.getMessage());
        assertEquals("success", result.getType());

        String url1 = urlPrefix + "/membership/1213";
        Map<String,Double> obj1=new HashMap<>();
        JsonResult result1 = restTemplate.postForObject(url1, obj1,JsonResult.class);
        assertEquals(400, result1.getCode());
        assertEquals("Invalid user id.", result1.getMessage());
        assertEquals("failed", result1.getType());
    }


    @Test
    void consumeBalance(){
        String url1 = urlPrefix + "/membership/set/5";
        Map<String,Double> obj1=new HashMap<>();
        obj1.put("balance",400.0);
        JsonResult result1;
        result1 = restTemplate.postForObject(url1, obj1, JsonResult.class);
        assertEquals(0, result1.getCode());
        assertEquals("Successfully join in the membership!", result1.getMessage());
        assertEquals("success", result1.getType());

        String url = urlPrefix + "/membership/consume/5";
        Map<String,Double> obj=new HashMap<>();
        obj.put("cost",200.0);
        JsonResult result = restTemplate.postForObject(url, obj,JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Successfully pay", result.getMessage());
        assertEquals("success", result.getType());

        String url3 = urlPrefix + "/membership/consume/5";
        Map<String,Double> obj3=new HashMap<>();
        obj3.put("cost",2000.0);
        JsonResult result3 = restTemplate.postForObject(url3, obj3,JsonResult.class);
        assertEquals(400, result3.getCode());
        assertEquals("Cost is not enough to pay", result3.getMessage());
        assertEquals("failed", result3.getType());

        String url2 = urlPrefix + "/membership/consume/5";
        Map<String,Double> obj2=new HashMap<>();
        obj2.put("cost",200.0);
        JsonResult result2 = restTemplate.postForObject(url2, obj2,JsonResult.class);
        assertEquals(0, result2.getCode());
        assertEquals("Successfully pay but set account to user", result2.getMessage());
        assertEquals("success", result2.getType());
    }

    @Test
    void rechargeMembership(){
        String url2 = urlPrefix + "/membership/recharge/3";
        Map<String,Double> obj2=new HashMap<>();
        obj2.put("recharge",400.0);
        JsonResult result2;
        result2 = restTemplate.postForObject(url2, obj2, JsonResult.class);
        assertEquals(0, result2.getCode());
        assertEquals("Successfully recharge", result2.getMessage());
        assertEquals("success", result2.getType());
    }
}
