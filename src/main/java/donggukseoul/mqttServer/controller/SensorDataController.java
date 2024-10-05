package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.enums.SensorType;
import donggukseoul.mqttServer.enums.SortBy;
import donggukseoul.mqttServer.enums.SortOrder;
import donggukseoul.mqttServer.service.SensorDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sensorData")
@RequiredArgsConstructor
public class SensorDataController {

    private final SensorDataService sensorDataService;

    @GetMapping
    public Map<String, Object> getSensorData(
            @RequestParam SensorType sensorType,
            @RequestParam(defaultValue = "TIMESTAMP") SortBy sortBy,
            @RequestParam(defaultValue = "ASC") SortOrder order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> response = sensorDataService.getSensorData(sensorType,sortBy,order,page,size);
        return response;
    }

    @GetMapping("/{sensorId}")
    public Map<String, Object> getSensorDataBySensorId(
            @RequestParam SensorType sensorType,
            @PathVariable String sensorId,
            @RequestParam(defaultValue = "TIMESTAMP") SortBy sortBy,
            @RequestParam(defaultValue = "ASC") SortOrder order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> response = sensorDataService.getSensorDataBySensorId(sensorType,sensorId,sortBy,order,page,size);
        return response;
    }

    @GetMapping("/recent/{sensorId}")
    public Map<String, Object> getRecentSensorData(@PathVariable String sensorId) {
        Map<String, Object> response = sensorDataService.getRecentSensorData(sensorId);
        return response;
    }
}
