package com.se2023.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.se2023.backend.entity.Activity.Activity;
import com.se2023.backend.entity.Others.TimeUnity;
import com.se2023.backend.mapper.ActivityMapper;
import com.se2023.backend.mapper.TimeUnityMapper;
import com.se2023.backend.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

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
        return new JsonResult(0, activityMapper.getActivity(), "Get activity", "success");
    }

    @PostMapping("/activity/{timeUnityId}")
    public JsonResult addActivity(@PathVariable("timeUnityId") Integer timeUnityId, @RequestBody Activity activity){
        //新建活动的时候，需要有一个时间单元的id，这个id是从前端传过来的
        activityMapper.addActivity(activity);
        Integer activityId = activityMapper.getActivityId(activity);
        activityMapper.addActivityTimeUnity(activityId, timeUnityId, activity.getUserAmount());
        return new JsonResult(0, null, "Add activity", "success");
    }

    @GetMapping("/activity/date_time/{date}/{startTime}/{endTime}")
    public JsonResult getActivityByTime(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime, @PathVariable("date") String date){
        //根据时间段获取活动, 先根据时间段获取时间单元，再根据时间单元获取活动
//        System.out.println(date + " " + startTime + " " + endTime);
        TimeUnity[] timeUnity = TimeUnityMapper.getTimeUnityByDateTime(date, startTime, endTime);
        System.out.println(Arrays.toString(timeUnity));
        Integer[] timeUnityId = new Integer[timeUnity.length];
        for (int i = 0; i < timeUnity.length; i++) {
            timeUnityId[i] = timeUnity[i].getId();
        }
        ArrayList<Activity> activity_list = new ArrayList<>();
        for (Integer value : timeUnityId) {
            Integer[] activity_id_list = activityMapper.getActivityByTimeUnity(value);
            for (Integer integer : activity_id_list) {
                Activity activity = activityMapper.getActivityById(integer);
                if (activity != null){
                    activity_list.add(activity);
                }
            }
        }

        return new JsonResult(0, activity_list, "Get activity by time", "success");
    }

    @GetMapping("/activity/facility/{id}")
    public JsonResult getActivityByFacility(@PathVariable("id") Integer id){
        //根据设施获取活动
        Activity[] activity_list = activityMapper.getActivityByFacilityId(id);
        ArrayList<JSONObject> result = new ArrayList<>();
        for (Activity activity : activity_list) {
            //根据活动id获取时间单元id
            int activityId = activity.getId();
            System.out.println(activityId);
            try {
                int timeUnityId = activityMapper.getTimeUnityIdByActivityId(activityId);
                //根据时间单元id获取开始时间和结束时间
                TimeUnity timeUnity = TimeUnityMapper.getTimeUnityById(timeUnityId);
                String startTime = timeUnity.getStartTime();
                String endTime = timeUnity.getEndTime();
                String date = timeUnity.getDate();
                JSONObject ac = new JSONObject();
                ac.put("activity", activity);
                ac.put("starttime", startTime);
                ac.put("endtime", endTime);
                ac.put("date", date);
                result.add(ac);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        return new JsonResult(0, result, "Get activity by facility", "success");
    }
}
