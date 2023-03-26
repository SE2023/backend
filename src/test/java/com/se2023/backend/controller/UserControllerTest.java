package com.se2023.backend.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.se2023.backend.config.EncryptionWithKeyConfig;
import com.se2023.backend.entity.User;
import com.se2023.backend.utils.JsonResult;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        String url;

        // valid email address
        url = urlPrefix + "/Consumer/1510397456@qq.com";
        JsonResult result = restTemplate.postForObject(url, null, JsonResult.class);
        assertEquals(400, result.getCode());
        assertEquals("send email success", result.getMessage());
        assertEquals("success", result.getType());

        // invalid email address
        url = urlPrefix + "/Consumer/invalid_email_address";
        result = restTemplate.postForObject(url, null, JsonResult.class);
        assertEquals(500, result.getCode());
        assertEquals("Something wrong", result.getMessage());
        assertEquals("failed", result.getType());
    }

    @Test
    void c_register() {
        String url = urlPrefix + "/Consumer";

        User user = new User();

        JsonResult result;

        // null username or password or email or confirmed code
        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(500, result.getCode());
        assertEquals("Something missing!", result.getMessage());
        assertEquals("failed", result.getType());

        user.setUsername("test");
        user.setPassword("123456");
        user.setConfirmCode("123456");
        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(500, result.getCode());
        assertEquals("Something missing!", result.getMessage());
        assertEquals("failed", result.getType());

        user.setUsername("test");
        user.setEmail("1510397456@qq.com");
        user.setConfirmCode("123456");
        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(500, result.getCode());
        assertEquals("Invalid confirm code", result.getMessage());
        assertEquals("failed", result.getType());

        // existing username
        user.setUsername("pangyu");
        user.setPassword("123456");
        user.setEmail("1510397456@qq.com");
        user.setConfirmCode("123456");
        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(500, result.getCode());
        assertEquals("This username is already used.", result.getMessage());
        assertEquals("failed", result.getType());
    }

    @Test
    void s_register() {
        String url = urlPrefix + "/Staff";

        User user = new User();

        JsonResult result;

        // null username or password or email or confirmed code
        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(500, result.getCode());
        assertEquals("Something missing!", result.getMessage());
        assertEquals("failed", result.getType());

        user.setUsername("test");
        user.setPassword("123456");
        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(500, result.getCode());
        assertEquals("Something missing!", result.getMessage());
        assertEquals("failed", result.getType());

        user.setUsername("test");
        user.setConfirmCode("123456");
        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(500, result.getCode());
        assertEquals("Invalid confirm code", result.getMessage());
        assertEquals("failed", result.getType());

        // existing username
        user.setUsername("pangyu");
        user.setPassword("123456");
        user.setConfirmCode("123456");
        result = restTemplate.postForObject(url, user, JsonResult.class);
        assertEquals(500, result.getCode());
        assertEquals("This username is already used.", result.getMessage());
        assertEquals("failed", result.getType());
    }

    @Test
    void queryUser() {
        String url = urlPrefix + "/users";

        JsonResult result = restTemplate.getForObject(url, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Query all users successfully.", result.getMessage());
        assertEquals("success", result.getType());
    }

    @Test
    void getUserInfo() {
        String url = urlPrefix + "/user";

        String username = "pangyu";
        String role = "staff";
        String token;
        // expire date
        Date date = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
        // algorithm
        Algorithm algorithm = Algorithm.HMAC256(EncryptionWithKeyConfig.KEY);
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        token = JWT.create()
                .withHeader(header)
                .withClaim("username", username)
                .withClaim("role", role)
                .withExpiresAt(date)
                .sign(algorithm);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Authorization", token);
        HttpEntity requestEntity = new HttpEntity(requestHeaders);
        ResponseEntity<JsonResult> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonResult.class
        );
        JsonResult result = response.getBody();
        assertEquals(0, result.getCode());
        assertEquals("Successfully achieved the user's info.", result.getMessage());
        assertEquals("success", result.getType());
    }

    @Test
    void queryAllNonmembers() {
        String url = urlPrefix + "/user/nonmembers";

        JsonResult result = restTemplate.getForObject(url, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Successfully achieved the nonmembers' info.", result.getMessage());
        assertEquals("success", result.getType());
    }

    @Test
    void queryAllStaffs() {
        String url = urlPrefix + "/user/staffs";

        JsonResult result = restTemplate.getForObject(url, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Successfully achieved the staffs' info.", result.getMessage());
        assertEquals("success", result.getType());
    }

    @Test
    void queryAllMembers() {
        String url = urlPrefix + "/user/members";

        JsonResult result = restTemplate.getForObject(url, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Successfully achieved the members' info.", result.getMessage());
        assertEquals("success", result.getType());
    }

    @Test
    void setMembership(){
        String url = urlPrefix + "/user/setMembership";

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
        String url = urlPrefix + "/user/removeMembership";

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
        String url = urlPrefix + "/user/membership";

        JsonResult result = restTemplate.getForObject(url, JsonResult.class);
        assertEquals(0, result.getCode());
        assertEquals("Successfully query all membership", result.getMessage());
        assertEquals("success", result.getType());
    }
}