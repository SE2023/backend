package com.se2023.backend.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.se2023.backend.entity.Order.Order;
import com.se2023.backend.mapper.OrderMapper;
import com.se2023.backend.utils.JsonResult;

@RestController
public class OrderController {
    private final OrderMapper orderMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    public OrderController(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @GetMapping("/order")
    public JsonResult getOrder(){
        return new JsonResult(0, orderMapper.getOrder(), "Get order", "success");
    }

    @GetMapping("/order/{id}")
    public JsonResult getOrderById(@PathVariable("id") Integer id){
        return new JsonResult(0, orderMapper.getOrderById(id), "Get order by id", "success");
    }

    @PostMapping("/order")
    public JsonResult addOrder(@RequestBody Order order) {
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
        orderMapper.addOrder(order);
        return new JsonResult(0, null, "Add order", "success");
    }
}
