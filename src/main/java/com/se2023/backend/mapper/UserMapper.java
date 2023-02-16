package com.se2023.backend.mapper;

import com.se2023.backend.entity.User.Consumer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username}")
    Consumer queryUserByUsername(String username);

    @Select("select * from user where id = #{id}")
    Consumer queryUserById(Integer id);

    @Select("select * from user")
    List<Consumer> queryAllUser();

     @Select("insert into user (username, password, role) values (#{username}, #{password}, #{role})")
     void addUser(Consumer user);
}
