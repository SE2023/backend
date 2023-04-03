package com.se2023.backend.entity.Others;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeUnity {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    String date;

    String startTime;

    String endTime;
}
