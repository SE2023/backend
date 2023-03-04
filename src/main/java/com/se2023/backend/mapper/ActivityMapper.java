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

    //修改活动
    @Select("update activity set name = #{name}, description = #{description}, location = #{location}, time = #{time}, type = #{type}, status = #{status} where id = #{id}")
    void updateActivity();

    @Select("select * from activity where id = #{id}")
    Activity getActivityById(Integer id);

    @Select("select * from activity")
    Activity[] getActivity();
}
