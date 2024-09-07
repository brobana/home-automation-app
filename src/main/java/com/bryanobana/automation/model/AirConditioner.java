package com.bryanobana.automation.model;

import lombok.Data;

import static com.bryanobana.automation.constant.Constants.DEVICE_NAME_AIRCONDITIONER;

@Data
public class AirConditioner {

    private final String device = DEVICE_NAME_AIRCONDITIONER;
    private String temperature;

}
