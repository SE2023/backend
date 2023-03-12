package com.se2023.backend.mapper;

import com.se2023.backend.entity.Activity.Facility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FacilityMapper {

    //添加设施
    @Select("insert into facility (name, sportsCentreId, capacity, status, type) values (#{name}, #{sportsCentreId}, #{capacity}, #{status}, #{type})")
    void addFacility(Facility facility);

    //删除设施
    @Select("delete from facility where id = #{id}")
    void deleteFacility();

    @Select("select * from facility where id = #{id}")
    Facility getFacilityById(Integer id);

    @Select("select * from facility")
    Facility[] getFacility();
}
