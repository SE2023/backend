package com.se2023.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.se2023.backend.entity.Activity.Activity;
import com.se2023.backend.entity.Activity.ActivityWithTime;
import com.se2023.backend.entity.Others.TimeUnity;
import com.se2023.backend.mapper.ActivityMapper;
import com.se2023.backend.mapper.TimeUnityMapper;
import com.se2023.backend.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.Arrays;
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
        return new JsonResult(0, activityMapper.getActivity(), "Get activity", "success");
    }

    @PostMapping("/activity/{timeUnityId}")
    public JsonResult addActivity(@PathVariable("timeUnityId") Integer timeUnityId, @RequestBody Activity activity){
        System.out.println("timeUnityId: "+ timeUnityId);
        //新建活动的时候，需要有一个时间单元的id，这个id是从前端传过来的
        //
        if (activityMapper.getActivityByName(activity.getName()) == null) {
            activityMapper.addActivity(activity);
        }
        Integer[] activityIds = activityMapper.getActivityId(activity);
        Integer activityId = activityIds[0];
        activityMapper.addActivityTimeUnity(activityId, timeUnityId, activity.getUserAmount());
        return new JsonResult(0, activity, "Add activity", "success");
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

    @GetMapping("/activity/all-with-time")
    public JsonResult getActivitiesWithTime() {
        ActivityWithTime[] activitiesWithTime = activityMapper.getActivityWithTime();

        return new JsonResult(0, activitiesWithTime, "Get activities with time", "success");
    }

    @GetMapping("/activity/{id}")
    public JsonResult getActivityById(@PathVariable("id") Integer id){
        //根据id获取活动
        Activity activity = activityMapper.getActivityById(id);
        return new JsonResult(0, activity, "Get activity by id", "success");
    }

    @GetMapping("/activity/date/{date}/activity/{activityId}")
    public JsonResult getTimeByDateAndActivity(@PathVariable("date") String date, @PathVariable("activityId") Integer activityId){
        //根据日期和活动id获取时间单元
        Integer[] UnityId = TimeUnityMapper.getTimeUnityByActivity(activityId);


        //根据时间单元id获取时间单元
        TimeUnity timeUnity = TimeUnityMapper.getTimeUnityById(UnityId[0]);
        //筛选日期相同的时间单元，存到一个数组里
        ArrayList<TimeUnity> timeUnity_list = new ArrayList<>();
        for (Integer integer : UnityId) {
            System.out.println("unityId " + integer);
            TimeUnity timeUnity1 = TimeUnityMapper.getTimeUnityById(integer);
            if (timeUnity1.getDate().equals(date)) {
                timeUnity_list.add(timeUnity1);
            }
        }
        System.out.println("timeUnity_list" + timeUnity_list);
        return new JsonResult(0, timeUnity_list, "Get time by date and activity", "success");
    }


    @GetMapping("/activity/facility/{id}")
    public JsonResult getActivityByFacility(@PathVariable("id") Integer id) {
        //根据设施获取活动
        Activity[] activity_list = activityMapper.getActivityByFacilityId(id);
        ArrayList<JSONObject> result = new ArrayList<>();
        System.out.println(id);
        System.out.println(activity_list.length);
        System.out.println("====================");
        System.out.print(Arrays.toString(activity_list));
        for (Activity activity : activity_list) {
            //根据活动id获取时间单元id
            int activityId = activity.getId();
            System.out.println("activityId, " + activityId);
            try {
                System.out.println("here");
//                Integer[] timeUnityIds = activityMapper.getTimeUnityIdByActivityId(activityId);
//                Integer timeUnityId = timeUnityIds[0];
//                System.out.println("timeUnityIds" + Arrays.toString(timeUnityIds));
                //根据时间单元id获取开始时间和结束时间
//                TimeUnity timeUnity = TimeUnityMapper.getTimeUnityById(timeUnityId);
//                String startTime = timeUnity.getStartTime();
//                String endTime = timeUnity.getEndTime();
//                String date = timeUnity.getDate();
                JSONObject ac = new JSONObject();

                // 如果ac中的activity的name和activity的id相同，就不加入
                boolean flag = false;
                for (JSONObject jsonObject : result) {
                    if (jsonObject.get("activity").equals(activity.getName())) {
                        flag = true;
                        break;
                    }
                }
                ac.put("activity", activity);
//                ac.put("starttime", startTime);
//                ac.put("endtime", endTime);
//                ac.put("date", date);
                System.out.println("ac" + ac);
                result.add(ac);
            } catch (Exception e) {
                break;
            }
        }
        System.out.println(result);
        return new JsonResult(0, result, "Get activity by facility", "success");
    }
}
