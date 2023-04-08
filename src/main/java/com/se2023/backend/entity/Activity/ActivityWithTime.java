package com.se2023.backend.entity.Activity;

import lombok.Data;

@Data
public class ActivityWithTime {
    String name;

    String facility;

    String facilityType;

    String price;

    String status;

    String note;

    Integer capacity;

    Integer peopleAmount;

    String date;

    String startTime;

    String endTime;
}
