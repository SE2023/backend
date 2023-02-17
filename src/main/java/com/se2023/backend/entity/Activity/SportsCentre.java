package com.se2023.backend.entity.Activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportsCentre {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    String name;

    String address;
}
