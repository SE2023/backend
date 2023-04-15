package com.se2023.backend.mapper;


import com.se2023.backend.entity.Coupon.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CouponMapper {
    @Select("insert into coupon (id, user_id, create_time, expire_time, discount) values (#{id}, #{user_id}, #{create_time}, #{expire_time}, #{discount})")
    void addCoupon(Coupon coupon);

    @Select("select * from coupon")
    List <Map<String,Object>> queryAllCoupon();

    @Select("select * from coupon where user_id = #{user_id}")
    List<Coupon> queryCouponByUser(Integer user_id);

    @Select("delete from coupon where user_id = #{user_id}")
    List<Coupon> deleteCouponOfUser(Integer user_id);

    @Select("select max(discount) from coupon where user_id= #{user_id}")
    Double queryMaxCouponOfUser(Integer user_id);

}
