package com.se2023.backend.entity.Order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.se2023.backend.entity.Others.TimeUnity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    String name;

    Integer activityId;

    Integer userId;

    String status;

    String note;

    Float payMoney;
}
