package com.se2023.backend.mapper;

import com.se2023.backend.entity.Email;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface EmailMapper {
    @Select("select * from email_confirm where email = #{email}")
    Email queryEmailByname(String email);

    @Select("select confirmCode from email_confirm where email = #{email}")
    String queryConfirmCodeByEmail(String email);

    @Select("insert into email_confirm (email,confirmCode) values (#{email}, #{confirmCode})")
    void addConfirm(Email email);

    @Select("update email_confirm set confirmCode = #{confirmCode} where email = #{email}")
    void updateConfirm(String confirmCode, String email);
}
