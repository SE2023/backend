package com.se2023.backend.mapper;

import org.apache.ibatis.annotations.Select;

public interface TimeUnityMapper {
    //添加时间单位
    @Select("insert into timeUnity (name, description, location, type, status) values (#{name}, #{description}, #{location}, #{type}, #{status})")
    void addTimeUnity();

    //删除时间单位
    @Select("delete from timeUnity where id = #{id}")
    void deleteTimeUnity();

    //修改时间单位
    @Select("update timeUnity set name = #{name}, description = #{description}, location = #{location}, type = #{type}, status = #{status} where id = #{id}")
    void updateTimeUnity();
}
