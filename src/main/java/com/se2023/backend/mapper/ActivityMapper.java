package com.se2023.backend.mapper;

import com.se2023.backend.entity.Activity.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ActivityMapper {
    //添加活动
    @Select("insert into activity (name, facilityId, price, note, userAmount, status) values (#{name}, #{facilityId}, #{price}, #{note}, #{userAmount}, #{status})")
    void addActivity(Activity activity);

    //删除活动
    @Select("delete from activity where id = #{id}")
    void deleteActivity();

    @Select("select * from activity where id = #{id}")
    Activity getActivityById(Integer id);

    @Select("select * from activity")
    Activity[] getActivity();

    @Select("select * from activity where facilityId = #{facilityId} and status = #{status} and userAmount = #{userAmount} and name = #{name}")
    Integer getActivityId(Activity activity);

    @Select("insert into activity_time (activityId, timeUnityId, peopleAmount) values (#{activityId}, #{timeUnityId}, #{peopleAmount})")
    void addActivityTimeUnity(Integer activityId, Integer timeUnityId, Integer peopleAmount);

    @Select("select * from activity_time where timeUnityId = #{timeUnityId}")
    Integer[] getActivityByTimeUnity(Integer timeUnityId);
}
