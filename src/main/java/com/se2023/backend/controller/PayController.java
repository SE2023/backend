package com.se2023.backend.controller;

import com.se2023.backend.mapper.OrderMapper;
import com.se2023.backend.mapper.UserMapper;
import com.se2023.backend.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayController {
    private final UserMapper userMapper;

    private final OrderMapper orderMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public PayController(UserMapper userMapper, OrderMapper orderMapper) {
        this.userMapper = userMapper;
        this.orderMapper = orderMapper;
    }

    @PutMapping("/pay/pay/{orderId}")
    public JsonResult pay(@PathVariable("orderId") Integer orderId) {
        //这里应该是调用支付接口，然后将订单状态改为已支付
        orderMapper.updateOrderStatus(orderId, "paid");
        //这里应该是调用支付接口，然后将用户余额减去订单金额
        //从订单中获取到用户信息
        Integer userId = orderMapper.getOrderById(orderId).getUserId();
        //从订单中获取到订单金额
        String orderAmount = orderMapper.getOrderById(orderId).getPayMoney();
        Integer amount = Integer.parseInt(orderAmount.split("/$")[1]);
        //从用户中获取到用户余额
        Double userBalance = userMapper.queryUserById(userId).getBalance();
        //将用户余额减去订单金额
        userMapper.updateBalance(userId, userBalance - amount);
        return new JsonResult(0, null, "Pay", "success");
    }

    @PutMapping("/pay/cancel/{orderId}")
    public JsonResult cancel(@PathVariable("orderId") Integer orderId) {
        //这里应该是调用支付接口，然后将订单状态改为取消
        orderMapper.updateOrderStatus(orderId, "cancelled");
        //这里从Redis中删除订单
        redisTemplate.delete(orderId.toString());
        return new JsonResult(0, null, "Cancel", "success");
    }
}
