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

import java.util.List;
import java.util.Map;

@Api(value="Coupon", tags = "Coupon Management")
@RestController
public class CouponController {
    private final CouponMapper couponMapper;
    private final MembershipMapper membershipMapper;
    private final UserMapper userMapper;

    @Autowired
    public CouponController(CouponMapper couponMapper, MembershipMapper membershipMapper, UserMapper userMapper){
        this.couponMapper=couponMapper;
        this.membershipMapper=membershipMapper;
        this.userMapper=userMapper;
    }


    @PostMapping(value="/coupon/{user_id}")
    public JsonResult addCoupon(@PathVariable("user_id") Integer id, @RequestBody Map<String, Double> map){
        Double discount = map.get("discount");
        if(discount==null){
            return new JsonResult(400,null,"Miss sonething","failed");
        }
        return new JsonResult(0,null,"Successfully add an coupon","success");
    }

    @GetMapping(value="/coupon/all")
    public JsonResult queryAllCoupon(){
        List<Coupon> coupons=couponMapper.queryAllCoupon();
        if(coupons==null){
            return new JsonResult(0,null,"No coupons","success");
        }else{
            return new JsonResult(0,coupons,"Successfully get all coupons","success");
        }
    }

    @GetMapping(value="/coupon/{user_id}")
    public JsonResult queryCouponByUser(@PathVariable("user_id") Integer user_id){
        List<Coupon> coupons=couponMapper.queryCouponByUser(user_id);
        if(coupons==null){
            return new JsonResult(0,null,"No coupons","success");
        }else{
            return new JsonResult(0,coupons,"Successfully get all coupons of this user","success");
        }
    }


    @PostMapping(value="/coupon/{user_id}")
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
