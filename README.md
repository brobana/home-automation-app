# home-automation-app
This application is a RESTful API that provides GET and PUT endpoints for retrieving and updating the status of devices.

Examples:
1. Increase Fan Speed
```
PUT /api/v1/devices/control

Request Body: {
  "device" : "fan"
  "action" : "increaseSpeed"
}

Response: {
  "result": "Fan speed: 1"
}
```

2. Check all device status
```
GET /api/v1/devices

Response: {
  "devices": [
    {
      "device": "Light",
      "status": "off"
    },
    {
      "device": "Fan",
      "speed": 1
    },
    {
      "device": "Air Conditioner",
      "temperature": "off"
    }
  ]
}
```

3. Check device status
```
GET /api/v1/devices/airconditioner

Response: {
  "device": "Air Conditioner",
  "temperature": "off"
}
```


For more information, see the Swagger UI: http://localhost:8080/swagger-ui/index.html
