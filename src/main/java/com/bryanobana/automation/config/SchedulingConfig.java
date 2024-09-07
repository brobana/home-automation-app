package com.bryanobana.automation.config;

import com.bryanobana.automation.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import static com.bryanobana.automation.constant.Actions.SWITCH_OFF;

@Configuration
@Slf4j
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {

    @Autowired
    DeviceService deviceService;

    @Value("${scheduling.cron}")
    private String cronExpression;

    // Couldn't use the @Value in the @Scheduled(cron = ...) annotation directly because Spring doesn't resolve
    // SpEL expressions (#{}) for cron expressions by default. This registers the task programmatically with the cron
    // expression fetched from the application-local.yml
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addCronTask(this::turnOffAllDevicesForUpgrade, cronExpression);
    }

    public void turnOffAllDevicesForUpgrade() {
        log.info("Turning off all devices for upgrade...");
        deviceService.updateLightStatus(SWITCH_OFF);
        deviceService.updateFanSpeed(0);
        deviceService.updateAirConditionerSetting(SWITCH_OFF);
    }
}
