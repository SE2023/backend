package com.se2023.backend.controller;

import com.se2023.backend.entity.Others.TimeUnity;
import com.se2023.backend.mapper.TimeUnityMapper;
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
    public void addTimeUnity(@RequestBody TimeUnity timeUnity){
        timeUnityMapper.addTimeUnity(timeUnity);
        Integer id = timeUnityMapper.getTimeUnityId(timeUnity);
    }
}
