package com.se2023.backend.entity.Activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Facility {

    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    String name;

    Integer sportsCentreId;

    Integer capacity;

    String status;

    Integer type;
}
