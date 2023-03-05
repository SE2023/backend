package com.se2023.backend.controller;

import com.se2023.backend.entity.Others.TimeUnity;
import com.se2023.backend.mapper.TimeUnityMapper;
import com.se2023.backend.utils.JsonResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeUnityController {
    private final TimeUnityMapper timeUnityMapper;

    public TimeUnityController(TimeUnityMapper timeUnityMapper) {
        this.timeUnityMapper = timeUnityMapper;
    }

    @PostMapping("/timeUnity")
    public JsonResult addTimeUnity(@RequestBody TimeUnity timeUnity){
        //新建时间单元，这应该在创建活动之前完成，前端因此要发两次请求
        timeUnityMapper.addTimeUnity(timeUnity);
        Integer id = timeUnityMapper.getTimeUnityId(timeUnity);
        return new JsonResult(200, id, "Add time unity", "success");
    }
}
