package com.se2023.backend.mapper;

import com.se2023.backend.entity.Others.TimeUnity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TimeUnityMapper {
    @Select("select * from time_unity where id = #{id}")
    TimeUnity getTimeUnityById(Integer id);

    @Select("select * from time_unity")
    TimeUnity[] getTimeUnity();

    @Select("select * from time_unity where activityId = #{activityId}")
    TimeUnity[] getTimeUnityByActivityId(Integer activityId);

    @Select("select * from time_unity where activityId = #{activityId} and status = #{status}")
    TimeUnity[] getTimeUnityByActivityIdAndStatus(Integer activityId, String status);

    @Select("select * from time_unity where startTime = #{startTime} and endTime = #{endTime}")
    TimeUnity[] getTimeUnityByTime(String startTime, String endTime);

    @Select("select * from time_unity where activityId = #{activityId} and status = #{status} and time = #{time}")
    TimeUnity[] getTimeUnityByActivityIdAndStatusAndTime(Integer activityId, String status, String time);

    @Select("insert into time_unity (activityId, status, time) values (#{activityId}, #{status}, #{time})")
    void addTimeUnity(TimeUnity timeUnity);

    @Select("delete from time_unity where id = #{id}")
    void deleteTimeUnity(Integer id);

    @Select("select id from time_unity where date = #{date} and startTime = #{startTime} and endTime = #{endTime}")
    Integer getTimeUnityId(TimeUnity timeUnity);
}
