package com.se2023.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.se2023.backend.entity.Order.Order;

@Mapper
public interface OrderMapper {
    @Select("select * from order where id = #{id}")
    Order getOrderById(Integer id);

    @Select("select * from order")
    Order[] getOrder();

    @Select("select * from order where userId = #{userId}")
    Order[] getOrdersByUserId(Integer userId);

    @Select("select * from order where activityId = #{activityId}")
    Order[] getOrdersByActivityId(Integer activityId);

    @Select("select * from order where status = #{status}")
    Order[] getOrdersByStatus(String status);

    @Select("select * from order where userId = #{userId} and status = #{status}")
    Order[] getOrdersByUserIdAndStatus(Integer userId, String status);

    @Select("select * from order where activityId = #{activityId} and status = #{status}")
    Order[] getOrdersByActivityIdAndStatus(Integer activityId, String status);

    @Select("select * from order where userId = #{userId} and activityId = #{activityId}")
    Order[] getOrdersByUserIdAndActivityId(Integer userId, Integer activityId);

    @Select("select * from order where userId = #{userId} and activityId = #{activityId} and status = #{status}")
    Order[] getOrdersByUserIdAndActivityIdAndStatus(Integer userId, Integer activityId, String status);

    @Select("insert into order (name, activityId, userId, status, note, payMoney) values (#{name}, #{activityId}, #{userId}, #{status}, #{note}, #{payMoney})")
    void addOrder(Order order);

    @Select("delete from order where id = #{id}")
    void deleteOrder(Integer id);
}
