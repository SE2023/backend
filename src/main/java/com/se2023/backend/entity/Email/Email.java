package com.se2023.backend.entity.Email;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Email {
    @TableId(type = IdType.AUTO)
    Integer id;
    String email;
    String confirmCode;

    public Email(String email, String confirmCode) {
        this.email = email;
        this.confirmCode = confirmCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", confirmCode='" + confirmCode + '\'' +
                '}';
    }
}






