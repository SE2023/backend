package com.se2023.backend.mapper;

import com.se2023.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username}")
    User queryUserByUsername(String username);

    @Select("select * from user where id = #{id}")
    User queryUserById(Integer id);

    @Select("select * from user")
    List<User> queryAllUser();

    @Select("insert into user (username, password) values (#{username}, #{password})")
    void addUser(User user);
}
