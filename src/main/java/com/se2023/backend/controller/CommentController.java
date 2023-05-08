package com.se2023.backend.controller;

import com.se2023.backend.entity.Comment.Comment;
import com.se2023.backend.mapper.CommentMapper;
import com.se2023.backend.mapper.FacilityMapper;
import com.se2023.backend.mapper.UserMapper;
import com.se2023.backend.utils.JsonResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Api(value="Comment", tags="Comment management")
@RestController
public class CommentController {
    private final UserMapper usermapper;
    private final CommentMapper commentMapper;
    private final FacilityMapper facilityMapper;

    @Autowired
    public CommentController(UserMapper userMapper,CommentMapper commentMapper, FacilityMapper facilityMapper){
        this.commentMapper=commentMapper;
        this.usermapper=userMapper;
        this.facilityMapper=facilityMapper;
    }

    @PostMapping("/comment/{facility_id}/{user_id}")
    public JsonResult addComment(@PathVariable("facility_id") Integer facility_id, @PathVariable("user_id") Integer user_id, @RequestBody String content){
        if(facilityMapper.getFacilityById(facility_id)!=null && usermapper.queryUserById(user_id)!=null && content!=null){
            Comment comment=new Comment(user_id,facility_id,content);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String create_time = sdf.format(new Date());
            comment.setCreate_time(create_time);
            commentMapper.addComment(comment);
            return new JsonResult(0,comment,"Success add this comment","success");
        }else{
            return new JsonResult(400,null,"Invalid facility id or user id","failed");
        }
    }

    @PostMapping("/comment/delete/{user_id}")
    public JsonResult deleteCommentByUser(@PathVariable("user_id") Integer user_id){
        if(commentMapper.queryCommentByUser(user_id)!=null){
            commentMapper.deleteCommentByUser(user_id);
            return new JsonResult(0,null,"Successfully delete all comments of this user.","success");
        }else{
            return new JsonResult(400,null,"This user have no comments.","failed");
        }
    }

    @PostMapping("/comment/delete")
    public JsonResult deleteComment(@RequestBody Comment comment){
        if(comment!=null){
            Integer id=comment.getId();
            commentMapper.deleteCommentById(id);
            return new JsonResult(0,null,"Successfully delete this comment","success");
        }else{
            return new JsonResult(400,null,"Invalid comment given information","failed");
        }
    }


    @GetMapping("/comment")
    public JsonResult allComment(){
        List<Comment> comments=commentMapper.queryAllComment();
        if(comments!=null){
            return new JsonResult(0,comments,"Success query all comments","success");
        }else{
            return new JsonResult(400,null,"No comments","fail");
        }
    }

    @GetMapping("/comment/{facility_id}")
    public JsonResult queryCommentByFacility(@PathVariable("facility_id") Integer facility_id){
        if(facilityMapper.getFacilityById(facility_id)!=null){
            List<Comment>comments=commentMapper.queryCommentByFacility(facility_id);
            return new JsonResult(0,comments,"Successfully query comment by facility","success");
        }else{
            return new JsonResult(400,null,"Invalid facility id.","failed");
        }
    }

}

