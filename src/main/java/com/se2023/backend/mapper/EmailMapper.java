package com.se2023.backend.mapper;

import com.se2023.backend.entity.Email;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmailMapper {
    @Select("select * from email where email = #{email}")
    Email queryEmailByName(String email);

    @Select("select confirmCode from email where email = #{email}")
    String queryConfirmCodeByEmail(String email);

    @Select("insert into email (email,confirmCode) values (#{email}, #{confirmCode})")
    void addConfirm(Email email);

    @Select("update email set confirmCode = #{confirmCode} where email = #{email}")
    void updateConfirm(String confirmCode, String email);
}
