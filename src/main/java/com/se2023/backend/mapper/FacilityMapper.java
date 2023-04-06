package com.se2023.backend.mapper;

import com.se2023.backend.entity.Activity.Facility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FacilityMapper {

    //添加设施
    @Select("insert into facility (name, sportsCentreId, status, type) values (#{name}, #{sportsCentreId},#{status}, #{type})")
    void addFacility(Facility facility);

    //删除设施
    @Select("delete from facility where id = #{id}")
    void deleteFacilityById(Integer id);

    @Select("select * from facility where id = #{id}")
    Facility getFacilityById(Integer id);

    @Select("select * from facility")
    Facility[] getFacility();

    @Select("update facility set name = #{facility.name}, sportsCentreId = #{facility.sportsCentreId}, status = #{facility.status}, type = #{facility.type} where id = #{id}")
    void updateFacilityById(Integer id, Facility facility);
}
