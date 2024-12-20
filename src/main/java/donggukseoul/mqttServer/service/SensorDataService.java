package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.dto.SensorDataDTO;
import donggukseoul.mqttServer.entity.SensorData;
import donggukseoul.mqttServer.enums.SensorType;
import donggukseoul.mqttServer.enums.SortBy;
import donggukseoul.mqttServer.enums.SortOrder;
import donggukseoul.mqttServer.exception.CustomException;
import donggukseoul.mqttServer.exception.ErrorCode;
import donggukseoul.mqttServer.repository.ClassroomRepository;
import donggukseoul.mqttServer.repository.SensorDataRepository;
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
        List<SensorData> recentData = sensorDataRepository.findTop10BySensorIdOrderByTimestampDesc(sensorId);

        Map<String, Object> response = new HashMap<>();
        response.put("data", recentData.stream().map(this::convertToDTO).collect(Collectors.toList()));
        return response;
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
                .filter(data -> data.getValue() > data.getThreshold())
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
        return SensorDataDTO.builder()
                .sensorId(sensorData.getSensorId())
                .sensorType(sensorData.getSensorType())
                .value(sensorData.getValue())
                .timestamp(sensorData.getTimestamp())
                .build();
    }
}
