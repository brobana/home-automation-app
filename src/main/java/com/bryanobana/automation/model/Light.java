package com.bryanobana.automation.model;

import lombok.Data;

import static com.bryanobana.automation.constant.Constants.DEVICE_NAME_LIGHT;

@Data
public class Light {

    private final String device = DEVICE_NAME_LIGHT;
    private String status;

}
