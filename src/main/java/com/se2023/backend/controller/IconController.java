package com.se2023.backend.controller;

import com.se2023.backend.entity.Icon.Icon;
import com.se2023.backend.mapper.IconMapper;
import com.se2023.backend.mapper.UserMapper;
import com.se2023.backend.utils.JsonResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

//import static sun.security.util.KnownOIDs.ContentType;


@Api(value="Icon", tags="Icon management")
@RestController
public class  IconController {
//    private final IconMapper iconMapper;
    private final UserMapper userMapper;
    private final IconMapper iconMapper;

    @Autowired
    public IconController(UserMapper userMapper, IconMapper iconMapper){
        this.iconMapper=iconMapper;
        this.userMapper=userMapper;
    }


    @PostMapping("/icon/load/{id}")
    public JsonResult loadIcon(@RequestParam("file")MultipartFile file, @PathVariable("id") Integer id){
        System.out.println("get in");
        if(file.isEmpty()){
            return new JsonResult(400,null,"No icon is selected","failed");
        }else{
            byte[] tar_icon = new byte[0];
            try {
                tar_icon = file.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String icon = Base64.getEncoder().encodeToString(tar_icon);
            Icon target_icon=new Icon(id,icon);
                //add icon to database
            if(iconMapper.queryIconById(id)!=null){
                iconMapper.updateIcon(id,icon);
                System.out.println("update successfully");
                return new JsonResult(0,file,"Upload the icon","success");
            }
            iconMapper.addIcon(target_icon);
            System.out.println("add successfully");
            return new JsonResult(0,file,"Upload the icon","success");

                //return new JsonResult(0,null,"Success to load icons for user","success");

        }
    }



}
