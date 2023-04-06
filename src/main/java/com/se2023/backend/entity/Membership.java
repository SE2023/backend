package com.se2023.backend.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Membership")
public class Membership {
    @TableId(type = IdType.AUTO)
    Integer user_id;
    String create_time;
    String expire_time;
    Double balance;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Membership{" +
                "user_id=" + user_id +
                ", create_time='" + create_time + '\'' +
                ", expire_time='" + expire_time + '\'' +
                ", balance=" + balance +
                '}';
    }
}
