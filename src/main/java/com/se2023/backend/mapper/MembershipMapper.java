package com.se2023.backend.mapper;

import com.se2023.backend.entity.Membership;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MembershipMapper {

    @Update("update user set membership=1 where id = #{id}")
    void setMembership(Integer id);

    @Update("update user set membership=null where id = #{id}")
    void removeMembership(Integer id);

    @Update("update membership set balance=#{remain} where user_id=#{user_id} ")
    void consumeBalance(Integer user_id,Integer remain);

    @Select("select * from membership")
    List<Membership> queryAllMembership();

    @Select("select * from membership where user_id = #{user_id}")
    Membership queryMembership(Integer user_id);

    @Select("insert into membership(user_id,create_time,expire_time, balance) values(#{user_id}, #{create_time}, #{expire_time}, #{balance})")
    void addMemebrship(Integer user_id, String create_time, String expire_time, Double balance);

    @Delete("delete from membership where user_id = #{id}")
    void deleteMembership(Integer id);

}
