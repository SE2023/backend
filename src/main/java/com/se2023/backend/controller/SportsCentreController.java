package com.se2023.backend.controller;

import com.se2023.backend.entity.Activity.SportsCentre;
import com.se2023.backend.mapper.SportsCentreMapper;
import com.se2023.backend.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
public class SportsCentreController {
    private final SportsCentreMapper sportsCentreMapper;

    @Autowired
    public SportsCentreController(SportsCentreMapper sportsCentreMapper) {
        this.sportsCentreMapper = sportsCentreMapper;
    }

    @GetMapping("/sports_centre/{id}")
    public JsonResult getSportsCentreById(@PathVariable("id") Integer id){
        SportsCentre sportsCentre = sportsCentreMapper.getSportsCentreById(id);
        return new JsonResult(0, sportsCentre, "Get sports centre by id", "success");
    }
}
