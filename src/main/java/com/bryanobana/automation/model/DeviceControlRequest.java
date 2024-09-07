package com.bryanobana.automation.model;

import lombok.Data;

@Data
public class DeviceControlRequest {
    private String device;
    private String action;
}
