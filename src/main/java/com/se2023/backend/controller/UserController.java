package com.se2023.backend.controller;

import com.se2023.backend.entity.User.Consumer;
import com.se2023.backend.mapper.UserMapper;
import com.se2023.backend.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/user")
    public JsonResult queryUser() {
        return new JsonResult(0, userMapper.queryAllUser(), "查询成功", "success");
    }

    @PostMapping("/user")
    public JsonResult addUser(@RequestBody Consumer user){
        try {
            userMapper.addUser(user);
            return new JsonResult(0, user, "添加成功", "success");
        } catch (Exception e) {
            return new JsonResult(500, null, "添加失败", "failed");
        }
    }
}
