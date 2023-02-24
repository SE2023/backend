package com.se2023.backend.entity.Activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    String name;

    Integer facilityId;

    Float price;

    String note;

    Integer userAmount;

    String status;
}
