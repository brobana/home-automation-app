package com.bryanobana.automation.controller;

import com.bryanobana.automation.model.*;
import com.bryanobana.automation.service.DeviceService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static com.bryanobana.automation.constant.Constants.*;

@RestController
@RequestMapping(BASE_PATH)
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @GetMapping(URI_LIGHT)
    public ResponseEntity<Light> getLightStatus() {
        Light light = deviceService.getLight();
        return ResponseEntity.ok().body(light);
    }

    @GetMapping(URI_FAN)
    public ResponseEntity<Fan> getFanSpeed() {
        Fan fan = deviceService.getFan();
        return ResponseEntity.ok().body(fan);
    }

    @GetMapping(URI_AC)
    public ResponseEntity<AirConditioner> getLight() {
        AirConditioner airConditioner = deviceService.getAirConditioner();
        return ResponseEntity.ok().body(airConditioner);
    }

    @GetMapping()
    public ResponseEntity<DevicesResponse> getAllDevices() {
        DevicesResponse response = new DevicesResponse();
        response.setDevices(new ArrayList<>());
        response.getDevices().add(deviceService.getLight());
        response.getDevices().add(deviceService.getFan());
        response.getDevices().add(deviceService.getAirConditioner());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(URI_CONTROL)
    public ResponseEntity<DeviceControlResponse> updateLightStatus(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DeviceControlRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Light: Turn On",
                                    value = "{ \"device\": \"light\", \"action\": \"switchOn\" }"
                            ),
                            @ExampleObject(
                                    name = "Light: Turn Off",
                                    value = "{ \"device\": \"light\", \"action\": \"switchOff\" }"
                            ),
                            @ExampleObject(
                                    name = "Fan: Increase Speed",
                                    value = "{ \"device\": \"fan\", \"action\": \"increaseSpeed\" }"
                            ),
                            @ExampleObject(
                                    name = "Fan: Decrease Speed",
                                    value = "{ \"device\": \"fan\", \"action\": \"decreaseSpeed\" }"
                            ),
                            @ExampleObject(
                                    name = "Air Conditioner: Increase Temperature",
                                    value = "{ \"device\": \"airconditioner\", \"action\": \"increaseTemperature\" }"
                            ),
                            @ExampleObject(
                                    name = "Air Conditioner: Decrease Temperature",
                                    value = "{ \"device\": \"airconditioner\", \"action\": \"decreaseTemperature\" }"
                            ),
                            @ExampleObject(
                                    name = "Air Conditioner: Turn Off",
                                    value = "{ \"device\": \"airconditioner\", \"action\": \"switchOff\" }"
                            )
                    })
    ) @RequestBody DeviceControlRequest request) {

        String deviceName = request.getDevice();
        DeviceControlResponse response = new DeviceControlResponse();

        try {
            if (deviceName.equalsIgnoreCase(DEVICE_NAME_LIGHT)) {
                response.setResult(deviceService.updateLightStatus(request.getAction()));
            } else if (deviceName.equalsIgnoreCase(DEVICE_NAME_FAN)) {
                response.setResult(deviceService.updateFanSpeed(request.getAction()));
            } else if (deviceName.equalsIgnoreCase(DEVICE_NAME_AIRCONDITIONER.replaceAll("\\s+", ""))) {
                response.setResult(deviceService.updateAirConditionerSetting(request.getAction()));
            } else {
                response.setResult("Error: Unknown device '" + deviceName + "'");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (RuntimeException rte) {
            response.setResult(rte.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok().body(response);
    }
}
