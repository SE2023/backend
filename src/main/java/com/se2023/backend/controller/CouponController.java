package com.se2023.backend.controller;

import com.se2023.backend.entity.Coupon.Coupon;
import com.se2023.backend.entity.User.User;
import com.se2023.backend.mapper.CouponMapper;
import com.se2023.backend.mapper.MembershipMapper;
import com.se2023.backend.mapper.UserMapper;
import com.se2023.backend.utils.JsonResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(value="Coupon", tags = "Coupon Management")
@RestController
public class CouponController {
    private final CouponMapper couponMapper;

    @Autowired
    public CouponController(CouponMapper couponMapper){
        this.couponMapper=couponMapper;
    }


    @PostMapping(value="/coupon/add/{user_id}")
    public JsonResult addCoupon(@PathVariable("user_id") Integer id, @RequestBody Map<String, Double> map){
        Double discount = map.get("discount");
        if(discount==null){
            return new JsonResult(400,null,"Miss sonething","failed");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String create_time = sdf.format(new Date());
        Date ex=new Date();
        ex.setTime(ex.getTime()+30*24*60*60*1000L );//优惠券默认保质期30天
        String expire_time=sdf.format(ex);
        Coupon coupon=new Coupon(id,create_time,expire_time,discount);
        couponMapper.addCoupon(coupon);
        return new JsonResult(0,coupon,"Successfully add an coupon","success");
    }

    @GetMapping(value="/coupon/all")
    public JsonResult queryAllCoupon(){
        List<Map<String,Object>> coupons=couponMapper.queryAllCoupon();
        if(coupons==null){
            return new JsonResult(0,null,"No coupons","success");
        }else{
            return new JsonResult(0,coupons,"Successfully get all coupons","success");
        }
    }

    @GetMapping(value="/coupon/{user_id}")
    public JsonResult queryCouponByUser(@PathVariable("user_id") Integer user_id){
        List<Coupon> targets=couponMapper.queryCouponByUser(user_id);
        System.out.println(targets);
        if(targets==null){
            return new JsonResult(0,null,"No coupons","success");
        }else{
            List<Map<String,Object>> coupons=couponMapper.queryAllCoupon();
            List<Map<String,Object>> res=new ArrayList<>();
            for (Map<String,Object> coupon:coupons){
                if (coupon.get("user_id").equals(user_id)){
                    res.add(coupon);
                }
            }
            return new JsonResult(0,res,"Successfully get all coupons of this user","success");
        }
    }


    @PostMapping(value="/coupon/delete/{user_id}")
    public JsonResult deleteCouponByUser(@PathVariable("user_id") Integer user_id){
        List<Coupon> coupons=couponMapper.queryCouponByUser(user_id);
        if(coupons==null){
            return new JsonResult(400,null,"No coupon for this user","failed");
        }else{
            couponMapper.deleteCouponOfUser(user_id);
            return new JsonResult(0,null,"Successfully delete coupons of this user","success");
        }
    }





}
