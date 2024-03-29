package com.se2023.backend.controller;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.se2023.backend.config.EncryptionWithKeyConfig;
import com.se2023.backend.entity.Activity.Activity;
import com.se2023.backend.entity.Activity.ActivityWithTime;
import com.se2023.backend.entity.Activity.Facility;
import com.se2023.backend.entity.Others.TimeUnity;
import com.se2023.backend.entity.User;
import com.se2023.backend.mapper.*;
import org.apache.tomcat.jni.Time;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.se2023.backend.entity.Order.Order;
import com.se2023.backend.utils.JsonResult;
import static com.se2023.backend.utils.EncryptionWithKey.decodeToken;

@RestController
public class OrderController {
    private final OrderMapper orderMapper;

    private final UserMapper userMapper;

    private final ActivityMapper activityMapper;
    private final TimeUnityMapper timeUnityMapper;
    private final FacilityMapper facilityMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    public OrderController(OrderMapper orderMapper, UserMapper userMapper, ActivityMapper activityMapper, TimeUnityMapper timeUnityMapper, FacilityMapper facilityMapper) {
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
        this.activityMapper = activityMapper;
        this.timeUnityMapper = timeUnityMapper;
        this.facilityMapper = facilityMapper;
    }

    @GetMapping("/order")
    public JsonResult getOrder(){
        Order[] orders = orderMapper.getOrder();
        Activity[] activities = new Activity[orders.length];
        Facility[] facilities = new Facility[orders.length];
        TimeUnity[] timeUnities = new TimeUnity[orders.length];
        User[] users = new User[orders.length];
        for (int i = 0; i < orders.length; i++) {
            activities[i] = activityMapper.getActivityById(orders[i].getActivityId());
            users[i] = userMapper.queryUserById(orders[i].getUserId());
            timeUnities[i] = timeUnityMapper.getTimeUnityById(activities[i].getId());
            facilities[i] = facilityMapper.getFacilityById(activities[i].getFacilityId());
        }
        String json = JSON.toJSONString(orders);
        String json2 = JSON.toJSONString(activities);
        String json3 = JSON.toJSONString(users);
        String json4 = JSON.toJSONString(timeUnities);
        String json5 = JSON.toJSONString(facilities);
        String json6 = "{\"orders\":" + json + ",\"activities\":" + json2 + ",\"users\":" + json3 + ",\"timeUnities\":" + json4 + ",\"facilities\":" + json5 + "}";
        return new JsonResult(0, json6, "Get order", "success");
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
        //获取活动的capacity
        Integer maxPeople = activityMapper.getCapacityByActivityId(activityId);
        //获取当前人数
        Integer currentPeople = (Integer)redisTemplate.opsForValue().get(activityId.toString());
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
        redisTemplate.opsForValue().set(activityId.toString(), currentPeople);
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
