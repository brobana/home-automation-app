package com.bryanobana.automation.config;

import com.bryanobana.automation.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDateTime;

import static com.bryanobana.automation.constant.Constants.DEVICE_ON;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class SchedulingConfigTest {

    @SpyBean
    SchedulingConfig schedulingConfig;

    @Value("${scheduling.cron}")
    private String cronExpression;

    @SpyBean
    DeviceService deviceService;


    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        // Overwrite ${scheduling.cron} dynamically so the method is executed few seconds after now().
        LocalDateTime currentDateTime = LocalDateTime.now();
        int day = currentDateTime.getDayOfMonth();
        int hour = currentDateTime.getHour();
        int minute = currentDateTime.getMinute();
        int second = currentDateTime.getSecond();

        // Schedules cron to run X seconds from now
        registry.add("scheduling.cron", () -> "%s %s %s %s * *".formatted(second + 5, minute, hour, day));
    }

    @BeforeEach
    void setUp() {
        deviceService.getLight().setStatus(DEVICE_ON);
        deviceService.getFan().setSpeed(2);
        deviceService.getAirConditioner().setTemperature(String.valueOf(23));
    }

    @Test
    void turnOffAllDevicesForUpgrade() {
        // Check if the initial device status are set
        assertEquals("on", deviceService.getLight().getStatus());
        assertEquals(2, deviceService.getFan().getSpeed());
        assertEquals("23", deviceService.getAirConditioner().getTemperature());

        // Sleep for X seconds to wait for cron to run
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            log.error("Error: Thread is interrupted while sleeping");
        }

        // Check if devices are updated and turned off
        assertEquals("off", deviceService.getLight().getStatus());
        assertEquals(0, deviceService.getFan().getSpeed());
        assertEquals("off", deviceService.getAirConditioner().getTemperature());
    }
}