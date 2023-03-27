package com.se2023.backend.mapper;

import com.se2023.backend.entity.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user where username = #{username}")
    List<User> queryUserByName(String username);
    @Select("select * from user where id = #{id}")
    User queryUserById(Integer id);

    @Select("select * from user where email = #{email}")
    User queryUserByEmail(String email);
    @Select("select * from user where username = #{username}")
    User queryUserByUsername(String username);

    @Select("select * from user where role = #{role}")
    List<User> queryUserByRole(String role);

    @Select("select email from user where confirmCode = #{confirmCode}")
    User selectUserByConfirmCode(@Param("confirmCode")String confirmCode);

    @Select("select * from user")
    List<User> queryAllUser();

    @Select("insert into user (username, password, email, role) values (#{username}, #{password}, #{email}, #{role})")
    void addUser(User user);


    @Update("update user set membership=1 where id = #{id}")
    void setMembership(Integer id);

    @Update("update user set membership=null where id = #{id}")
    void removeMembership(Integer id);

    @Select("select * from membership")
    List<User> queryAllMembership();

    @Select("select user_id from membership where user_id = #{user_id}")
    Integer queryMembership(Integer user_id);

    @Select("insert into membership(user_id,create_time,expire_time) values(#{user_id}, #{create_time}, #{expire_time})")
    void addMemebrship(Integer user_id, String create_time, String expire_time);

    @Delete("delete from membership where user_id = #{id}")
    void deleteMembership(Integer id);

}
