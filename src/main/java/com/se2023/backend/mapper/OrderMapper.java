package com.se2023.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

    //添加订单
    @Select("insert into order (name, description, location, time, type, status) values (#{name}, #{description}, #{location}, #{time}, #{type}, #{status})")
    void addOrder();

    //删除订单
    @Select("delete from order where id = #{id}")
    void deleteOrder();

    //修改订单
    @Select("update order set name = #{name}, description = #{description}, location = #{location}, time = #{time}, type = #{type}, status = #{status} where id = #{id}")
    void updateOrder();

}
