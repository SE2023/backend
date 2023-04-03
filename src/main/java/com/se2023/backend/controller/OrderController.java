package com.se2023.backend.controller;

import cn.hutool.core.date.DateTime;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.se2023.backend.config.EncryptionWithKeyConfig;
import com.se2023.backend.entity.User;
import com.se2023.backend.mapper.UserMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.se2023.backend.entity.Order.Order;
import com.se2023.backend.mapper.OrderMapper;
import com.se2023.backend.utils.JsonResult;
import static com.se2023.backend.utils.EncryptionWithKey.decodeToken;

@RestController
public class OrderController {
    private final OrderMapper orderMapper;

    private final UserMapper userMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    public OrderController(OrderMapper orderMapper, UserMapper userMapper) {
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
    }

    @GetMapping("/order")
    public JsonResult getOrder(){
        return new JsonResult(0, orderMapper.getOrder(), "Get order", "success");
    }

    @GetMapping("/order/{id}")
    public JsonResult getOrderById(@PathVariable("id") Integer id){
        return new JsonResult(0, orderMapper.getOrderById(id), "Get order by id", "success");
    }

    @GetMapping("/order/token")
    public JsonResult getOrderById(@RequestHeader("Authorization") String token){
        //使用SHA256对token解码
        DecodedJWT jwt = decodeToken(token);
        String username = jwt.getClaim("username").asString();
        User user = userMapper.queryUserByUsername(username);
        Integer id = user.getId();
        return new JsonResult(0, orderMapper.getOrdersByUserId(id), "Get order by token", "success");
    }

    @PostMapping("/order")
    public JsonResult addOrder(@RequestBody Order order, @RequestHeader("Authorization") String token) {
        //新建一个订单，这里没有考虑人数上限的问题
        //先获取活动的id
        Integer activityId = order.getActivityId();
        //通过活动id拿到facility的id
        Integer facilityId = orderMapper.getFacilityId(activityId);
        //获取活动的人数上限
        Integer maxPeople = orderMapper.getCapacity(facilityId);
        //获取当前人数
        Integer currentPeople = (Integer)redisTemplate.opsForValue().get(facilityId.toString());
        //如果当前人数已经达到上限，就不创建订单
        if (currentPeople == null) {
            currentPeople = 0;
        }
        //如果当前人数没有达到上限，就创建订单
        if (currentPeople >= maxPeople) {
            return new JsonResult(1, null, "Add order", "fail");
        } else {
            currentPeople = currentPeople + 1;
        }
        //同时人数上限+1, 记录在redis
        redisTemplate.opsForValue().set(facilityId.toString(), currentPeople);
        //创建订单
        //先获取用户的id
        //通过token获取用户的id
        //使用SHA256对token解码
        DecodedJWT jwt = decodeToken(token);
        String username = jwt.getClaim("username").asString();
        User user = userMapper.queryUserByUsername(username);
        Integer userId = user.getId();
        order.setUserId(userId);
        //获取时间
        DateTime dateTime = new DateTime();
        //将时间转化成字符串
        order.setTime(dateTime.toString());
        //设置订单状态维unpaid
        order.setStatus("unpaid");
        order.setRemark("unpaid");
        orderMapper.addOrder(order);
        return new JsonResult(0, null, "Add order", "success");
    }
}
