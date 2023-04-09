package com.se2023.backend.controller;

import com.se2023.backend.entity.Activity.Facility;
import com.se2023.backend.mapper.FacilityMapper;
import com.se2023.backend.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FacilityController{
    private final FacilityMapper facilityMapper;

    @Autowired
    public FacilityController(FacilityMapper facilityMapper) {
        this.facilityMapper = facilityMapper;
    }

    @PostMapping("/facility")
    public JsonResult addFacility(@RequestBody Facility facility){
        facilityMapper.addFacility(facility);
        return new JsonResult(0, null, "Add facility", "success");
    }

    @GetMapping("/facility")
    public JsonResult getFacility(){
        return new JsonResult(0, facilityMapper.getFacility(), "Get facility", "success");
    }

    @GetMapping("/facility/{id}")
    public JsonResult getFacilityById(@PathVariable("id") Integer id){
        return new JsonResult(0, facilityMapper.getFacilityById(id), "Get facility by id", "success");
    }

    @DeleteMapping("/facility/{id}")
    public JsonResult deleteFacilityById(@PathVariable("id") Integer id){
        facilityMapper.deleteFacilityById(id);
        return new JsonResult(0, null, "Delete facility by id", "success");
    }

    @PutMapping("/facility/{id}")
    public JsonResult updateFacilityById(@PathVariable("id") Integer id, @RequestBody Facility facility){
        System.out.println(facility);
        facilityMapper.updateFacilityById(id, facility);
        return new JsonResult(0, null, "Update facility by id", "success");
    }
}
