package com.bryanobana.automation.service;

import com.bryanobana.automation.model.AirConditioner;
import com.bryanobana.automation.model.Fan;
import com.bryanobana.automation.model.Light;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.bryanobana.automation.constant.Actions.*;
import static com.bryanobana.automation.constant.Constants.DEVICE_OFF;
import static com.bryanobana.automation.constant.Constants.DEVICE_ON;

@Service
@Slf4j
public class DeviceService {

    @Getter
    private final Light light;
    @Getter
    private final Fan fan;
    @Getter
    private final AirConditioner airConditioner;

    @Value("${fan.speed.max}")
    private int fanMaxSpeed;

    @Value("${fan.speed.min}")
    private int fanMinSpeed;

    @Value("${airconditioner.temperature.max}")
    private int acMaxTemperature;

    @Value("${airconditioner.temperature.min}")
    private int acMinTemperature;

    public DeviceService() {
        // When the app is started, get the actual device status.
        this.light = new Light();
        this.light.setStatus(DEVICE_OFF);
        this.fan = new Fan();
        this.fan.setSpeed(fanMinSpeed);
        this.airConditioner = new AirConditioner();
        this.airConditioner.setTemperature(DEVICE_OFF);
    }

    public String updateLightStatus(String action) {
        String result;
        if (action.equalsIgnoreCase(SWITCH_ON)) {
            if (light.getStatus().equalsIgnoreCase(DEVICE_OFF)) light.setStatus(DEVICE_ON);
        } else if (action.equalsIgnoreCase(SWITCH_OFF)) {
            if (light.getStatus().equalsIgnoreCase(DEVICE_ON)) light.setStatus(DEVICE_OFF);
        } else {
            String error = "Error: Unknown action '" + action + "'";
            throw new RuntimeException(error);
        }
        result = "%s status: %s".formatted(light.getDevice(), light.getStatus());
        log.info(result);
        return result;
    }

    public String updateFanSpeed(String action) {
        int fanSpeed = fan.getSpeed();
        if (action.equalsIgnoreCase(INCREASE_SPEED)) {
            if (fanSpeed < fanMaxSpeed) {
                ++fanSpeed;
            }
        } else if (action.equalsIgnoreCase(DECREASE_SPEED)) {
            if (fanSpeed > fanMinSpeed) {
                --fanSpeed;
            }
        } else {
            String error = "Error: Unknown action '" + action + "'";
            throw new RuntimeException(error);
        }
        fan.setSpeed(fanSpeed);
        String result = fan.getSpeed() == 0 ?
                "%s speed: off".formatted(fan.getDevice()) :
                "%s speed: %s".formatted(fan.getDevice(), fan.getSpeed());
        log.info(result);
        return result;
    }

    public String updateFanSpeed(int speed) {
        if (speed <= fanMaxSpeed && speed >= fanMinSpeed) {
            fan.setSpeed(speed);
        } else {
            String error = "Error: Invalid fan speed '" + speed + "'";
            throw new RuntimeException(error);
        }
        String result = fan.getSpeed() == 0 ?
                "%s speed: off".formatted(fan.getDevice()) :
                "%s speed: %s".formatted(fan.getDevice(), fan.getSpeed());
        log.info(result);
        return result;
    }

    public String updateAirConditionerSetting(String action) {
        int temperature = !airConditioner.getTemperature().equalsIgnoreCase(DEVICE_OFF) ?
                Integer.parseInt(airConditioner.getTemperature()) : 0;
        if (action.equalsIgnoreCase(INCREASE_TEMPERATURE)) {
            if (temperature < acMaxTemperature) {
                if (temperature == 0) temperature = acMinTemperature - 1;
                ++temperature;
            }
        } else if (action.equalsIgnoreCase(DECREASE_TEMPERATURE)) {
            if (temperature == 0) temperature = acMaxTemperature + 1;
            if (temperature > acMinTemperature) --temperature;

        } else if (action.equalsIgnoreCase(SWITCH_OFF)) {
            temperature = 0;
        } else {
            String error = "Error: Unknown action '" + action + "'";
            throw new RuntimeException(error);
        }
        airConditioner.setTemperature(
                temperature == 0 ? "off" : String.valueOf(temperature)
        );
        String result = airConditioner.getTemperature().equalsIgnoreCase("off") ?
                "%s temperature: off".formatted(airConditioner.getDevice()) :
                "%s temperature: %s".formatted(airConditioner.getDevice(), airConditioner.getTemperature());
        log.info(result);
        return result;
    }

}
