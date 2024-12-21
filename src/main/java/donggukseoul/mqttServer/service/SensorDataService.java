package donggukseoul.mqttServer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import donggukseoul.mqttServer.dto.SensorDataDTO;
import donggukseoul.mqttServer.entity.SensorData;
import donggukseoul.mqttServer.entity.SensorTypeEntity;
import donggukseoul.mqttServer.enums.SensorType;
import donggukseoul.mqttServer.enums.SortBy;
import donggukseoul.mqttServer.enums.SortOrder;
import donggukseoul.mqttServer.exception.CustomException;
import donggukseoul.mqttServer.exception.ErrorCode;
import donggukseoul.mqttServer.repository.ClassroomRepository;
import donggukseoul.mqttServer.repository.SensorDataRepository;
import donggukseoul.mqttServer.repository.SensorTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;
    private final ClassroomRepository classroomRepository;
    private final SensorTypeRepository sensorTypeRepository;

    // 센서 데이터 가져오기
    public Map<String, Object> getSensorData(SensorType sensorType, SortBy sortBy, SortOrder order, int page, int size) {
        Sort.Direction sortDirection = (order == SortOrder.ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy.getFieldName());

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SensorData> sensorDataPage = sensorDataRepository.findBySensorType(sensorType.getTypeName(), pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("sensorType", sensorType.getTypeName());
        response.put("data", sensorDataPage.getContent().stream().map(this::convertToDTO).collect(Collectors.toList()));
        return response;
    }

    // 특정 센서 ID로 데이터 가져오기
    public Map<String, Object> getSensorDataBySensorId(SensorType sensorType, String sensorId, SortBy sortBy, SortOrder order, int page, int size) {
        Sort.Direction sortDirection = (order == SortOrder.ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy.getFieldName());

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SensorData> sensorDataPage = sensorDataRepository.findBySensorIdAndSensorType(sensorId, sensorType.getTypeName(), pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("sensorType", sensorType.getTypeName());
        response.put("data", sensorDataPage.getContent().stream().map(this::convertToDTO).collect(Collectors.toList()));
        return response;
    }

    // 건물과 강의실 이름으로 센서 데이터 가져오기
    public Map<String, Object> getSensorDataByBuildingAndName(SensorType sensorType, String building, String name, SortBy sortBy, SortOrder order, int page, int size) {
        String sensorId = classroomRepository.findSensorIdByBuildingAndName(building, name);
        if (sensorId == null) {
            throw new CustomException(ErrorCode.SENSOR_NOT_FOUND);
        }
        return getSensorDataBySensorId(sensorType, sensorId, sortBy, order, page, size);
    }

    // 최근 센서 데이터 가져오기
    public Map<String, Object> getRecentSensorData(String sensorId) {
        List<SensorTypeEntity> sensorTypes = sensorTypeRepository.findAll();
        Map<String, Object> groupedData = new HashMap<>();

        for (SensorTypeEntity sensorType : sensorTypes) {
            SensorData recentData = sensorDataRepository.findTopBySensorIdAndSensorTypeOrderByTimestampDesc(sensorId, sensorType.getTypeName());
            if (recentData != null) {
                if ("AQMScores".equalsIgnoreCase(recentData.getSensorType())) {
                    groupedData.put("AQMScores", parseAqmScores(recentData));
                } else {
                    groupedData.put(recentData.getSensorType(), convertToDTO(recentData));
                }
            }
        }

        return groupedData;
    }


    private Map<String, Object> parseAqmScores(SensorData data) {
        Map<String, Object> aqmScores = new HashMap<>();
        aqmScores.put("sensorId", data.getSensorId());
        aqmScores.put("timestamp", data.getTimestamp());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(data.getValueString());

            aqmScores.put("temperatureStatus", jsonNode.get("temperature").asText());
            aqmScores.put("humidityStatus", jsonNode.get("humidity").asText());
            aqmScores.put("tvocStatus", jsonNode.get("tvoc").asText());
            aqmScores.put("pm25Status", jsonNode.get("PM2_5MassConcentration").asText());
            aqmScores.put("ambientNoiseStatus", jsonNode.get("ambientNoise").asText());
            aqmScores.put("co2Status", jsonNode.get("CO2").asText());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.PARSING_ERROR);
        }

        return aqmScores;
    }


    // 최근 센서 데이터 가져오기 (건물 및 강의실 이름 기반)
    public Map<String, Object> getRecentSensorDataByBuildingAndName(String building, String name) {
        String sensorId = classroomRepository.findSensorIdByBuildingAndName(building, name);
        if (sensorId == null) {
            throw new CustomException(ErrorCode.SENSOR_NOT_FOUND);
        }
        return getRecentSensorData(sensorId);
    }

    // 비정상 센서 데이터 가져오기 (예: 최근 1시간 내)
    public List<SensorDataDTO> getAbnormalValuesOverTheLastHour() {
        LocalDateTime oneHourAgo = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusHours(1);

        List<SensorData> recentData = sensorDataRepository.findByTimestampAfter(oneHourAgo);

        return recentData.stream()
                .filter(data -> {
                    try {
                        // value가 null이면 필터링에서 제외
                        if (data.getValue() == null) {
                            return false;
                        }
                        // 임계값과 비교
                        return data.getValue() > data.getThreshold();
                    } catch (IllegalArgumentException e) {
                        // Threshold가 정의되지 않은 타입은 필터링에서 제외
                        return false;
                    }
                })
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }




    public Map<String, Object> getCombinedSensorData(
            List<SensorType> sensorTypes,
            String building,
            String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            SortOrder order,
            int page,
            int size
    ) {
        // 센서 ID를 가져옴
        String sensorId = classroomRepository.findSensorIdByBuildingAndName(building, name);
        if (sensorId == null) {
            throw new CustomException(ErrorCode.SENSOR_NOT_FOUND);
        }

        // 정렬 방향 설정
        Sort.Direction sortDirection = (order == SortOrder.ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "timestamp"));

        // 선택된 센서 타입에 따라 데이터 가져오기
        List<SensorData> combinedData = sensorDataRepository.findBySensorIdAndSensorTypeInAndTimestampBetween(
                sensorId,
                sensorTypes.stream().map(SensorType::getTypeName).collect(Collectors.toList()),
                startDate,
                endDate,
                pageable
        ).getContent();

        // DTO로 변환
        List<SensorDataDTO> sensorDataDTOs = combinedData.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("data", sensorDataDTOs);
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalElements", combinedData.size());
        response.put("totalPages", (combinedData.size() + size - 1) / size);

        return response;
    }



    private boolean isAbnormal(SensorData data) {
        return data.getValue() > data.getThreshold(); // 예제 조건, 실제 조건에 맞게 수정 필요
    }

    // DTO 변환 메서드
    private SensorDataDTO convertToDTO(SensorData sensorData) {
        String building = classroomRepository.findBySensorId(sensorData.getSensorId()).get().getBuilding();
        String name = classroomRepository.findBySensorId(sensorData.getSensorId()).get().getName();

        return SensorDataDTO.builder()
                .sensorId(sensorData.getSensorId())
                .sensorType(sensorData.getSensorType())
                .value(sensorData.getValue())
                .timestamp(sensorData.getTimestamp())
                .building(building)
                .name(name)
                .level(null) // 필요하면 레벨 로직 추가
                .build();
    }

}
