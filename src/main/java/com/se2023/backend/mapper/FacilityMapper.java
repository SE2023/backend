package com.se2023.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FacilityMapper {

    //添加设施
    @Select("insert into facility (name, description, location, type, status) values (#{name}, #{description}, #{location}, #{type}, #{status})")
    void addFacility();

    //删除设施
    @Select("delete from facility where id = #{id}")
    void deleteFacility();

    //修改设施
    @Select("update facility set name = #{name}, description = #{description}, location = #{location}, type = #{type}, status = #{status} where id = #{id}")
    void updateFacility();
}
