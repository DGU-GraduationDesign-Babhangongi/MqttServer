package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.dto.SensorDataDTO;
import donggukseoul.mqttServer.enums.SensorType;
import donggukseoul.mqttServer.enums.SortBy;
import donggukseoul.mqttServer.enums.SortOrder;
import donggukseoul.mqttServer.service.SensorDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
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

    @GetMapping("/classroom")
    public Map<String, Object> getSensorDataByBuildingAndName(
            @RequestParam SensorType sensorType,
            @RequestParam String building,
            @RequestParam String name,
            @RequestParam(defaultValue = "TIMESTAMP") SortBy sortBy,
            @RequestParam(defaultValue = "ASC") SortOrder order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> response = sensorDataService.getSensorDataByBuildingAndName(sensorType,building,name,sortBy,order,page,size);
        return response;
    }

    @GetMapping("/classroom/betweenDates")
    public Map<String, Object> getSensorDataBetweenDates(
            @RequestParam List<SensorType> sensorTypes,
            @RequestParam String building,
            @RequestParam String name,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam(defaultValue = "ASC") SortOrder order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
//        Map<String, Object> response = new HashMap<>();
//
//        for (SensorType sensorType : sensorTypes) {
//            Map<String, Object> sensorData = sensorDataService.getSensorDataBetweenDates(
//                    sensorType, building, name, startDate, endDate, sortBy, order, page, size);
//            response.put(sensorType.name(), sensorData);
//        }
//
//        return response;

//        Map<String, Object> response = new HashMap<>();
        Map<String, Object> sensorData = sensorDataService.getCombinedSensorData(sensorTypes, building, name, startDate, endDate, order, page, size);

        return sensorData;

    }

    @GetMapping("/recent/{sensorId}")
    public Map<String, Object> getRecentSensorData(@PathVariable String sensorId) {
        Map<String, Object> response = sensorDataService.getRecentSensorData(sensorId);
        return response;
    }

    @GetMapping("/recent/classroom")
    public Map<String, Object> getRecentSensorDataByBuildingAndName(
            @RequestParam String building,
            @RequestParam String name
    ) {

        Map<String, Object> response = sensorDataService.getRecentSensorDataByBuildingAndName(building, name);

        return response;
    }

    @GetMapping("/abnormalValues")
    public ResponseEntity<List<SensorDataDTO>> getAbnormalValuesOverTheLastHour() {
        List<SensorDataDTO> anomalies = sensorDataService.getAbnormalValuesOverTheLastHour();
        return ResponseEntity.ok(anomalies);
    }

}
