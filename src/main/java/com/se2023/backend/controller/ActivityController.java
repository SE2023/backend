package com.se2023.backend.controller;

import com.se2023.backend.entity.Activity.Activity;
import com.se2023.backend.entity.Others.TimeUnity;
import com.se2023.backend.mapper.ActivityMapper;
import com.se2023.backend.mapper.TimeUnityMapper;
import com.se2023.backend.utils.JsonResult;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ActivityController {
    private final ActivityMapper activityMapper;
    private final TimeUnityMapper TimeUnityMapper;

    @Autowired
    public ActivityController(ActivityMapper activityMapper, TimeUnityMapper TimeUnityMapper) {
        this.activityMapper = activityMapper;
        this.TimeUnityMapper = TimeUnityMapper;
    }

    @GetMapping("/activity")
    public JsonResult getActivity(){
        return new JsonResult(200, activityMapper.getActivity(), "Get activity", "success");
    }

    @PostMapping("/activity/{timeUnityId}")
    public JsonResult addActivity(@PathVariable("timeUnityId") Integer timeUnityId, @RequestBody Activity activity){
        //新建活动的时候，需要有一个时间单元的id，这个id是从前端传过来的
        activityMapper.addActivity(activity);
        Integer activityId = activityMapper.getActivityId(activity);
        activityMapper.addActivityTimeUnity(activityId, timeUnityId, activity.getUserAmount());
        return new JsonResult(200, null, "Add activity", "success");
    }

    @GetMapping("/activity/{startTime}/{endTime}")
    public JsonResult getActivityByTime(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime){
        //根据时间段获取活动, 先根据时间段获取时间单元，再根据时间单元获取活动
        TimeUnity[] timeUnity = TimeUnityMapper.getTimeUnityByTime(startTime, endTime);
        Integer[] timeUnityId = new Integer[timeUnity.length];
        for (int i = 0; i < timeUnity.length; i++) {
            timeUnityId[i] = timeUnity[i].getId();
        }
        List<Activity> activity_list = null;
        for (int i = 0; i < timeUnityId.length; i++) {
            Integer[] activity_id_list = activityMapper.getActivityByTimeUnity(timeUnityId[i]);
            for (int j = 0; j < activity_id_list.length; j++) {
                Activity activity = activityMapper.getActivityById(activity_id_list[j]);
                activity_list.add(activity);
            }
        }
        return new JsonResult(200, activity_list, "Get activity by time", "success");
    }
}
