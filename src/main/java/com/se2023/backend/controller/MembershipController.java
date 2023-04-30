package com.se2023.backend.controller;

import com.se2023.backend.entity.Membership.Membership;
import com.se2023.backend.entity.User.User;
import com.se2023.backend.mapper.MembershipMapper;
import com.se2023.backend.mapper.UserMapper;
import com.se2023.backend.utils.JsonResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @PostMapping(value="/membership/set/{id}")
    @ResponseBody
    public JsonResult setMembership(@PathVariable("id") Integer id, @RequestBody Map<String,Double> map){
        Double balance= map.get("balance");
        if (balance==null){
            return new JsonResult(400,null,"Balance can not be less than 100 dollars","failed");
        }
        if (balance<100){
            return new JsonResult(400,null,"Balance can not be less than 100 dollars","failed");
        }
        User user = userMapper.queryUserById(id);
        if(user!=null){
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
                Membership member=new Membership(user_id,create_time,expire_time,balance);
                membershipMapper.addMemebrship(member);
                return new JsonResult(0, member,"Successfully join in the membership!", "success");

            }
            else{
                System.out.println(membershipMapper.queryMembership(user_id));
                return new JsonResult(400, null,"You are already a membership", "failed");
            }
        }
        else{
            return  new JsonResult(400,null,"Something missing!","failed");
        }
    }

    @PostMapping(value="/membership/remove/{id}")
    public JsonResult removeMembership(@PathVariable("id") Integer id){
        if(userMapper.queryUserById(id)==null){
            return new JsonResult(400,null,"Invalid user id.","failed");
        }
        else {
            User user = userMapper.queryUserById(id);
            if (user != null) {
                Integer user_id = user.getId();
                if (membershipMapper.queryMembership(user_id) != null) {
                    membershipMapper.removeMembership(user.getId());
                    membershipMapper.deleteMembership(user.getId());
                    return new JsonResult(0, null, "Successfully remove this membership!", "success");
                } else {
                    return new JsonResult(500, null, "Invalid membership!", "failed");
                }
            } else {
                return new JsonResult(500, null, "Missing user id!", "failed");
            }
        }
    }

    @GetMapping(value="/membership")
    public JsonResult queryAllMembership(){
        List<Map<String,Object>> members=membershipMapper.queryAllMembership();
        return new JsonResult(0, members,"Successfully query all membership", "success");
    }

    @PostMapping(value="/membership/{id}")
    public JsonResult queryMembership(@PathVariable("id") Integer id){
        if(userMapper.queryUserById(id)==null){
            return new JsonResult(400,null,"Invalid user id.","failed");
        }else{
            List<Map<String,Object>> members=membershipMapper.queryAllMembership();
            for (Map<String,Object> member:members){
                if (member.get("user_id").equals(id)){
                    return new JsonResult(0,member,"Successfully get membership","success");
                }
            }
            return new JsonResult(400,null,"Invalid membership id.","failed");
        }
    }

    @GetMapping(value="/membership/{id}")
    public JsonResult queryMembershipById(@PathVariable("id") Integer id){
        if(membershipMapper.queryMembership(id)==null){
            return new JsonResult(400,null,"Invalid membership id.","failed");
        }else{
            Membership member= membershipMapper.queryMembership(id);
            return new JsonResult(0,member,"Successfully get membership","success");
        }
    }

    @PostMapping(value="/membership/consume/{id}")
    public JsonResult consumeBalance(@PathVariable("id") Integer id,@RequestBody Map<String,Double> map){
        Double cost= map.get("cost");
        if(membershipMapper.queryMembership(id)==null){
            return new JsonResult(400,null,"Invalid membership id.","failed");
        }else{
            Membership member= membershipMapper.queryMembership(id);
            Double balance = member.getBalance();
            if(balance<cost){
                return new JsonResult(400,null,"Cost is not enough to pay","failed");
            }else if(balance.equals(cost)){
                membershipMapper.removeMembership(id);
                membershipMapper.deleteMembership(id);
                return new JsonResult(0,member.getBalance(),"Successfully pay but set account to user","success");
            }else{
                member.setBalance(balance-cost);
                membershipMapper.consumeBalance(id,balance-cost);
                return new JsonResult(0,member.getBalance(),"Successfully pay","success");

            }
        }
    }

    @PostMapping(value="/membership/recharge/{id}")
    public JsonResult rechargeBalance(@PathVariable("id") Integer id,@RequestBody Map<String,Double> map){
        Double recharge= map.get("recharge");
        if(membershipMapper.queryMembership(id)==null){
            return new JsonResult(400,null,"Invalid membership id.","failed");
        }else{
            Membership member= membershipMapper.queryMembership(id);
            Double balance = member.getBalance();
            member.setBalance(balance+recharge);
            membershipMapper.consumeBalance(id, (int) (balance+recharge));
            return new JsonResult(0,member,"Successfully recharge","success");
        }
    }

}
