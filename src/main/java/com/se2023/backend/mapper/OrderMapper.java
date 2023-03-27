package com.se2023.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.se2023.backend.entity.Order.Order;

@Mapper
public interface OrderMapper {
    @Select("select * from orders where id = #{id}")
    Order getOrderById(Integer id);

    @Select("select * from orders")
    Order[] getOrder();

    @Select("select * from orders where userId = #{userId}")
    Order[] getOrdersByUserId(Integer userId);

    @Select("select * from orders where activityId = #{activityId}")
    Order[] getOrdersByActivityId(Integer activityId);

    @Select("select * from orders where status = #{status}")
    Order[] getOrdersByStatus(String status);

    @Select("select * from orders where userId = #{userId} and status = #{status}")
    Order[] getOrdersByUserIdAndStatus(Integer userId, String status);

    @Select("select * from orders where activityId = #{activityId} and status = #{status}")
    Order[] getOrdersByActivityIdAndStatus(Integer activityId, String status);

    @Select("select * from orders where userId = #{userId} and activityId = #{activityId}")
    Order[] getOrdersByUserIdAndActivityId(Integer userId, Integer activityId);

    @Select("select * from orders where userId = #{userId} and activityId = #{activityId} and status = #{status}")
    Order[] getOrdersByUserIdAndActivityIdAndStatus(Integer userId, Integer activityId, String status);

    @Select("insert into orders (name, activityId, userId, status, remark, payMoney, time) values (#{name}, #{activityId}, #{userId}, #{status}, #{remark}, #{payMoney}, #{time})")
    void addOrder(Order order);

    @Select("delete from orders where id = #{id}")
    void deleteOrder(Integer id);

    @Select("select capacity from facility where id = #{id}")
    Integer getCapacity(Integer id);

    @Select("select facilityId from activity where id = #{id}")
    Integer getFacilityId(Integer id);
}
