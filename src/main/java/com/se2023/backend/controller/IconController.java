package com.se2023.backend.controller;

import com.se2023.backend.entity.Icon.Icon;
import com.se2023.backend.mapper.IconMapper;
import com.se2023.backend.mapper.UserMapper;
import com.se2023.backend.utils.JsonResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
public class IconController {
    private final IconMapper iconMapper;
    private final UserMapper userMapper;

    @Autowired
    public IconController(UserMapper userMapper, IconMapper iconMapper){
        this.iconMapper=iconMapper;
        this.userMapper=userMapper;
    }

    @PostMapping("/icon/load/{id}")
    public JsonResult loadIcon(@RequestParam("file")MultipartFile file, @PathVariable("id") Integer id){
        if(file.isEmpty()){
            return new JsonResult(400,null,"No icon is selected","failed");
        }else{
            System.out.println(file);
            try {
                byte[] tar_icon = file.getBytes();
                String icon = Base64.getEncoder().encodeToString(tar_icon);
                Icon target_icon=new Icon(id,icon);
                iconMapper.addIcon(target_icon);
                return new JsonResult(0,null,"Success to load icons for user","success");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @GetMapping("/icon/upload/{id}")
    public JsonResult getImage(@PathVariable("id") Integer id) {
        // 从数据库中根据图片id获取图片的byte[]数据
        String iconBase=iconMapper.queryIconById(id);
        byte[] icon = Base64.getDecoder().decode(iconBase);

        // 将byte[]数据转换为InputStream
        InputStream inputStream = new ByteArrayInputStream(icon);

        // 设置响应头，指定图片的Content-Type和Content-Disposition
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // 设置图片类型
        headers.setContentDisposition(ContentDisposition.inline().filename("image.jpg").build()); // 设置图片文件名

        // 创建InputStreamResource对象，将InputStream和响应头一起作为参数传入
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        // 创建ResponseEntity对象，将InputStreamResource和响应头一起作为参数传入
        //return new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK);
        return new JsonResult(0,inputStreamResource,"Upload the icon","success");
    }

}
