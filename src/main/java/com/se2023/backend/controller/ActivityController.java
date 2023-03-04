package com.se2023.backend.controller;

import com.se2023.backend.entity.Activity.Activity;
import com.se2023.backend.mapper.ActivityMapper;
import com.se2023.backend.utils.JsonResult;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ActivityController {
    private final ActivityMapper activityMapper;

    @Autowired
    public ActivityController(ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }

    @GetMapping("/activity")
    public JsonResult getActivity(){
        return new JsonResult(200, activityMapper.getActivity(), "Get activity", "success");
    }

    @PostMapping("/activity")
    public JsonResult addActivity(@RequestBody Activity activity){
        activityMapper.addActivity(activity);
        return new JsonResult(200, null, "Add activity", "success");
    }
}
