package com.se2023.backend.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ActivityMapper {
    //添加活动
    @Select("insert into activity (name, description, location, time, type, status) values (#{name}, #{description}, #{location}, #{time}, #{type}, #{status})")
    void addActivity();

    //删除活动
    @Select("delete from activity where id = #{id}")
    void deleteActivity();

    //修改活动
    @Select("update activity set name = #{name}, description = #{description}, location = #{location}, time = #{time}, type = #{type}, status = #{status} where id = #{id}")
    void updateActivity();
}
