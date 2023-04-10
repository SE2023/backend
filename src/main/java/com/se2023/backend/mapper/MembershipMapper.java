package com.se2023.backend.mapper;

import com.se2023.backend.entity.Membership;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface MembershipMapper {

    @Update("update user set membership=1 where id = #{id}")
    void setMembership(Integer id);


    @Select("insert into membership(user_id,create_time,expire_time, balance) values (#{user_id}, #{create_time}, #{expire_time}, #{balance})")
    void addMemebrship(Membership member);

    @Delete("delete from membership where user_id = #{id}")
    void deleteMembership(Integer id);

    @Update("update user set membership=null where id = #{id}")
    void removeMembership(Integer id);

    @Update("update membership set balance=#{remain} where user_id=#{user_id} ")
    void consumeBalance(Integer user_id, double remain);


    @Select("select * from membership")
    List <Map<String,Object>> queryAllMembership();

    @Select("select * from membership where user_id = #{user_id}")

//    Map<String,Object> queryMembership(Integer user_id);
    Membership queryMembership(Integer user_id);

}
