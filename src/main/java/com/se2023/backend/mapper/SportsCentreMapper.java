package com.se2023.backend.mapper;

import com.se2023.backend.entity.Activity.SportsCentre;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SportsCentreMapper {
    //添加场馆
    @Select("insert into sports_centre (name, description, location, type, status) values (#{name}, #{description}, #{location}, #{type}, #{status})")
    void addSportsCentre();

    //删除场馆
    @Select("delete from sports_centre where id = #{id}")
    void deleteSportsCentre();

    //修改场馆
    @Select("update sports_centre set name = #{name}, description = #{description}, location = #{location}, type = #{type}, status = #{status} where id = #{id}")
    void updateSportsCentre();

    @Select("select * from sports_centre where id = #{id}")
    SportsCentre getSportsCentreById(Integer id);
}
