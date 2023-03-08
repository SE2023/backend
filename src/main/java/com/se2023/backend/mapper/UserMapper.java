package com.se2023.backend.mapper;

import com.se2023.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where id = #{id}")
    User queryUserById(Integer id);

    @Select("select * from user where email = #{email}")
    User queryUserByEmail(String email);
    @Select("select * from user where username = #{username}")
    User queryUserByUsername(String username);

    @Select("select * from user where role = #{role}")
    User queryUserByRole(String role);

    @Select("select email from user where confirmCode = #{confirmCode}")
    User selectUserByConfirmCode(@Param("confirmCode")String confirmCode);

    @Select("select * from user")
    List<User> queryAllUser();

    @Select("insert into user (username, password, email, role) values (#{username}, #{password}, #{email}, #{role})")
    void addUser(User user);
}
