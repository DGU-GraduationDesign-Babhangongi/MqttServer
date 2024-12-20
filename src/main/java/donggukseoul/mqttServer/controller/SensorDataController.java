package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.dto.SensorDataDTO;
import donggukseoul.mqttServer.enums.SensorType;
import donggukseoul.mqttServer.enums.SortBy;
import donggukseoul.mqttServer.enums.SortOrder;
import donggukseoul.mqttServer.exception.CustomException;
import donggukseoul.mqttServer.exception.ErrorCode;
import donggukseoul.mqttServer.service.SensorDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sensorData")
@RequiredArgsConstructor
public class SensorDataController {

    private final SensorDataService sensorDataService;

    @GetMapping
    public Map<String, Object> getSensorData(
            @RequestParam String sensorType,
            @RequestParam(defaultValue = "TIMESTAMP") SortBy sortBy,
            @RequestParam(defaultValue = "ASC") SortOrder order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        SensorType type = SensorType.fromTypeName(sensorType);
        if (type == null) {
            throw new CustomException(ErrorCode.INVALID_SENSOR_TYPE);
        }

        return sensorDataService.getSensorData(type, sortBy, order, page, size);
    }

    @GetMapping("/{sensorId}")
    public Map<String, Object> getSensorDataBySensorId(
            @RequestParam String sensorType,
            @PathVariable String sensorId,
            @RequestParam(defaultValue = "TIMESTAMP") SortBy sortBy,
            @RequestParam(defaultValue = "ASC") SortOrder order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        SensorType type = SensorType.fromTypeName(sensorType);
        if (type == null) {
            throw new CustomException(ErrorCode.INVALID_SENSOR_TYPE);
        }

        return sensorDataService.getSensorDataBySensorId(type, sensorId, sortBy, order, page, size);
    }

    @GetMapping("/classroom")
    public Map<String, Object> getSensorDataByBuildingAndName(
            @RequestParam String sensorType,
            @RequestParam String building,
            @RequestParam String name,
            @RequestParam(defaultValue = "TIMESTAMP") SortBy sortBy,
            @RequestParam(defaultValue = "ASC") SortOrder order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        SensorType type = SensorType.fromTypeName(sensorType);
        if (type == null) {
            throw new CustomException(ErrorCode.INVALID_SENSOR_TYPE);
        }

        return sensorDataService.getSensorDataByBuildingAndName(type, building, name, sortBy, order, page, size);
    }

    @GetMapping("/classroom/betweenDates")
    public Map<String, Object> getSensorDataBetweenDates(
            @RequestParam List<String> sensorTypes,
            @RequestParam String building,
            @RequestParam String name,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam(defaultValue = "ASC") SortOrder order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<SensorType> types = sensorTypes.stream()
                .map(SensorType::fromTypeName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (types.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_SENSOR_TYPE);
        }

        return sensorDataService.getCombinedSensorData(types, building, name, startDate, endDate, order, page, size);
    }

    @GetMapping("/recent/{sensorId}")
    public Map<String, Object> getRecentSensorData(@PathVariable String sensorId) {
        return sensorDataService.getRecentSensorData(sensorId);
    }

    @GetMapping("/recent/classroom")
    public Map<String, Object> getRecentSensorDataByBuildingAndName(
            @RequestParam String building,
            @RequestParam String name
    ) {
        return sensorDataService.getRecentSensorDataByBuildingAndName(building, name);
    }

    @GetMapping("/abnormalValues")
    public ResponseEntity<List<SensorDataDTO>> getAbnormalValuesOverTheLastHour() {
        List<SensorDataDTO> anomalies = sensorDataService.getAbnormalValuesOverTheLastHour();
        return ResponseEntity.ok(anomalies);
    }
}
