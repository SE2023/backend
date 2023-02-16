package com.se2023.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SportsCentre {
    //添加场馆
    @Select("insert into sportsCentre (name, description, location, type, status) values (#{name}, #{description}, #{location}, #{type}, #{status})")
    void addSportsCentre();

    //删除场馆
    @Select("delete from sportsCentre where id = #{id}")
    void deleteSportsCentre();

    //修改场馆
    @Select("update sportsCentre set name = #{name}, description = #{description}, location = #{location}, type = #{type}, status = #{status} where id = #{id}")
    void updateSportsCentre();
}
