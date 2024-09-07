package com.bryanobana.automation.service;

import com.bryanobana.automation.config.SchedulingConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static com.bryanobana.automation.constant.Actions.*;
import static com.bryanobana.automation.constant.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeviceServiceTest {

    @SpyBean
    DeviceService deviceService;

    @MockBean
    SchedulingConfig schedulingConfig;

    @BeforeEach
    void setUp() {
        deviceService.getLight().setStatus(DEVICE_OFF);
        deviceService.getFan().setSpeed(0);
        deviceService.getAirConditioner().setTemperature(DEVICE_OFF);
    }

    @Test
    void updateLightStatus() {
        String dummyAction = "DummyAction";
        assertEquals("Light status: on", deviceService.updateLightStatus(SWITCH_ON));
        assertEquals("Light status: off", deviceService.updateLightStatus(SWITCH_OFF));
        Exception exception = assertThrows(RuntimeException.class, () ->
                deviceService.updateLightStatus(dummyAction));
        assertEquals("Error: Unknown action '" + dummyAction + "'", exception.getMessage());
    }

    @Test
    void updateFanSpeedByIncreasingDecreasingSpeed() {
        String dummyAction = "DummyAction";
        assertEquals("Fan speed: 1", deviceService.updateFanSpeed(INCREASE_SPEED));
        assertEquals("Fan speed: off", deviceService.updateFanSpeed(DECREASE_SPEED));
        Exception exception = assertThrows(RuntimeException.class, () ->
                deviceService.updateFanSpeed(dummyAction));
        assertEquals("Error: Unknown action '" + dummyAction + "'", exception.getMessage());
    }

    @Test
    void updateFanSpeedBySettingSpeed() {
        int invalidFanSpeed = 4;
        assertEquals("Fan speed: 2", deviceService.updateFanSpeed(2));
        Exception exception = assertThrows(RuntimeException.class, () ->
                deviceService.updateFanSpeed(invalidFanSpeed));
        assertEquals("Error: Invalid fan speed '" + invalidFanSpeed + "'", exception.getMessage());
    }

    @Test
    void updateAirConditionerSetting() {
        String dummyAction = "DummyAction";
        assertEquals("Air Conditioner temperature: 18", deviceService.updateAirConditionerSetting(INCREASE_TEMPERATURE));
        assertEquals("Air Conditioner temperature: 19", deviceService.updateAirConditionerSetting(INCREASE_TEMPERATURE));
        assertEquals("Air Conditioner temperature: 18", deviceService.updateAirConditionerSetting(DECREASE_TEMPERATURE));
        assertEquals("Air Conditioner temperature: off", deviceService.updateAirConditionerSetting(SWITCH_OFF));
        Exception exception = assertThrows(RuntimeException.class, () ->
                deviceService.updateAirConditionerSetting(dummyAction));
        assertEquals("Error: Unknown action '" + dummyAction + "'", exception.getMessage());
    }

}