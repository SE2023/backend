package com.se2023.backend.controller;

import com.se2023.backend.entity.Membership;
import com.se2023.backend.entity.User;
import com.se2023.backend.mapper.MembershipMapper;
import com.se2023.backend.mapper.UserMapper;
import com.se2023.backend.utils.JsonResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.text.SimpleDateFormat;
import java.util.Date;

@Api(value="Membership", tags = "Membership Management")
@RestController
public class MembershipController {
    private final UserMapper userMapper;
    private final MembershipMapper membershipMapper;

    @Autowired
    public MembershipController(UserMapper userMapper, MembershipMapper membershipMapper){
        this.userMapper=userMapper;
        this.membershipMapper = membershipMapper;
    }

    @PostMapping(value="/membership")
    public JsonResult setMembership(@RequestBody User user, Integer balance){
        if (balance<=100){
            return new JsonResult(400,null,"Balance can not under 100 dollars","failed");
        }
        if(user.getId()!=null){
            //check 是否已经是membership
            Integer user_id=user.getId();
            if (membershipMapper.queryMembership(user_id)==null){
                //user表里set membership为1
                membershipMapper.setMembership(user.getId());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String create_time = sdf.format(new Date());
                Date ex=new Date();
                ex.setTime(ex.getTime()+365*24*60*60*1000L );//会员默认保质期一年
                String expire_time=sdf.format(ex);
                //membership表里add对象
                membershipMapper.addMemebrship(user_id,create_time,expire_time, balance);
                return new JsonResult(0, null,"Successfully join in the membership!", "success");
            }
            else{
                System.out.println(membershipMapper.queryMembership(user_id));
                return new JsonResult(400, null,"You are already a membership", "fail");
            }
        }
        else{
            return  new JsonResult(400,null,"Something missing!","fail");
        }
    }

    @PostMapping(value="/membership/{id}")
    public JsonResult removeMembership(@PathVariable("id") Integer id){
        if(userMapper.queryUserById(id)==null){
            return new JsonResult(400,null,"Invalid user id.","failed");
        }
        else {
            User user = userMapper.queryUserById(id);
            if (user.getId() != null) {
                Integer user_id = user.getId();
                if (membershipMapper.queryMembership(user_id) != null) {
                    membershipMapper.removeMembership(user.getId());
                    membershipMapper.deleteMembership(user.getId());
                    return new JsonResult(0, null, "Successfully remove this membership!", "success");
                } else {
                    return new JsonResult(500, null, "Invalid membership!", "fail");
                }
            } else {
                return new JsonResult(500, null, "Missing user id!", "fail");
            }
        }
    }

    @GetMapping(value="/membership")
    public JsonResult queryAllMembership(){
        return new JsonResult(0, membershipMapper.queryAllMembership(),"Successfully query all membership", "success");
    }

    @GetMapping(value="/membership/{id}")
    public JsonResult queryMembership(@PathVariable("id") Integer id){
        if(userMapper.queryUserById(id)==null){
            return new JsonResult(400,null,"Invalid user id.","failed");
        }else{
            Membership member =membershipMapper.queryMembership(id);
            return new JsonResult(0,member,"Successfully get membership","success");
        }
    }

    @PostMapping(value="/membership/consume/{id}")
    public JsonResult consumeBalance(@PathVariable("id") Integer id,Integer cost){
        if(membershipMapper.queryMembership(id)==null){
            return new JsonResult(400,null,"Invalid membership id.","failed");
        }else{
            Membership member=membershipMapper.queryMembership(id);
            Integer balance = member.getBalance();
            if(balance<cost){
                return new JsonResult(400,null,"Cost is not enough to pay","failed");
            }else if(balance.equals(cost)){
                membershipMapper.removeMembership(id);
                membershipMapper.deleteMembership(id);
                return new JsonResult(0,member,"Successfully pay but set account to user","success");
            }else{
                member.setBalance(balance-cost);
                return new JsonResult(0,member,"Successfully pay","success");
            }
        }
    }
}
