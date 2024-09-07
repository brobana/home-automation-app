package com.bryanobana.automation.model;

import lombok.Data;

import static com.bryanobana.automation.constant.Constants.DEVICE_NAME_FAN;

@Data
public class Fan {

    private final String device = DEVICE_NAME_FAN;
    private int speed;

}
