package com.se2023.backend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public OrderController(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @GetMapping("/order")
    public JsonResult getOrder(){
        return new JsonResult(200, orderMapper.getOrder(), "Get order", "success");
    }

    @GetMapping("/order/{id}")
    public JsonResult getOrderById(@PathVariable("id") Integer id){
        return new JsonResult(200, orderMapper.getOrderById(id), "Get order by id", "success");
    }

    @PostMapping("/order")
    public JsonResult addOrder(@RequestBody Order order){
        //新建一个订单，这里没有考虑人数上限的问题
        orderMapper.addOrder(order);
        return new JsonResult(200, null, "Add order", "success");
    }
}
