package donggukseoul.mqttServer.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import donggukseoul.mqttServer.dto.AqmScoreDTO;
import donggukseoul.mqttServer.dto.SensorDataDTO;
import donggukseoul.mqttServer.entity.*;
import donggukseoul.mqttServer.enums.SensorType;
import donggukseoul.mqttServer.enums.SortBy;
import donggukseoul.mqttServer.enums.SortOrder;
import donggukseoul.mqttServer.exception.CustomException;
import donggukseoul.mqttServer.exception.ErrorCode;
import donggukseoul.mqttServer.repository.*;
import donggukseoul.mqttServer.util.SensorDataChecker;
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
    private final ClassroomRepository classroomRepository;


    private final SensorDataTemperatureRepository sensorDataTemperatureRepository;
    private final SensorDataTvocRepository sensorDataTvocRepository;
    private final SensorDataAmbientNoiseRepository sensorDataAmbientNoiseRepository;
    private final SensorDataIaqIndexRepository sensorDataIaqIndexRepository;
    private final SensorDataAqmScoresRepository sensorDataAqmScoresRepository;
    private final SensorDataHumidityRepository sensorDataHumidityRepository;
    private final SensorDataUsbPoweredRepository sensorDataUsbPoweredRepository;
    private final SensorDataButtonPressedRepository sensorDataButtonPressedRepository;
    private final SensorDataWaterDetectionRepository sensorDataWaterDetectionRepository;
    private final SensorDataPm2_5MassConcentrationRepository sensorDataPm2_5MassConcentrationRepository;
    private final SensorDataChecker sensorDataChecker;

    public Map<String, Object> getSensorData(
            SensorType sensorType,
            SortBy sortBy,
            SortOrder order,
            int page,
            int size
    ) {
        // 정렬 방향 설정
        Sort.Direction sortDirection = order == SortOrder.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy.getFieldName());

        // Pageable 객체 생성 (페이징 및 정렬)
        Pageable pageable = PageRequest.of(page, size, sort);

        // 응답을 위한 Map 초기화
        Map<String, Object> response = new HashMap<>();
        Page<?> sensorDataPage;

        // 센서 타입에 따라 적절한 리포지토리 호출 및 데이터 변환
        switch (sensorType) {
            case TEMPERATURE:
                sensorDataPage = sensorDataTemperatureRepository.findAll(pageable);
                response.put("sensorType", "Temperature");
                break;
            case TVOC:
                sensorDataPage = sensorDataTvocRepository.findAll(pageable);
                response.put("sensorType", "TVOC");
                break;
            case AMBIENT_NOISE:
                sensorDataPage = sensorDataAmbientNoiseRepository.findAll(pageable);
                response.put("sensorType", "AmbientNoise");
                break;
            case IAQ_INDEX:
                sensorDataPage = sensorDataIaqIndexRepository.findAll(pageable);
                response.put("sensorType", "IAQIndex");
                break;
            case AQM_SCORES:
                sensorDataPage = sensorDataAqmScoresRepository.findAll(pageable);
                response.put("sensorType", "AQMScores");
                break;
            case HUMIDITY:
                sensorDataPage = sensorDataHumidityRepository.findAll(pageable);
                response.put("sensorType", "Humidity");
                break;
            case USB_POWERED:
                sensorDataPage = sensorDataUsbPoweredRepository.findAll(pageable);
                response.put("sensorType", "UsbPowered");
                break;
            case BUTTON_PRESSED:
                sensorDataPage = sensorDataButtonPressedRepository.findAll(pageable);
                response.put("sensorType", "ButtonPressed");
                break;
            case WATER_DETECTION:
                sensorDataPage = sensorDataWaterDetectionRepository.findAll(pageable);
                response.put("sensorType", "WaterDetection");
                break;
            case PM2_5_MASS_CONCENTRATION:
                sensorDataPage = sensorDataPm2_5MassConcentrationRepository.findAll(pageable);
                response.put("sensorType", "PM2_5MassConcentration");
                break;
            default:
                throw new CustomException(ErrorCode.INVALID_SENSOR_TYPE);
        }

        response.put("data", convertToDTO(sensorDataPage.getContent()));
        return response;
    }

    public Map<String, Object> getCombinedSensorData(List<SensorType> sensorTypes, String building, String name, LocalDateTime startDate, LocalDateTime endDate, SortOrder order, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        List<SensorDataDTO> combinedData = new ArrayList<>();

        String sensorId = classroomRepository.findSensorIdByBuildingAndName(building, name);
        if (sensorId == null) {
            throw new CustomException(ErrorCode.SENSOR_NOT_FOUND);
        }

        for (SensorType sensorType : sensorTypes) {
            switch (sensorType) {
                case ALL:
                    combinedData.addAll(sensorDataTemperatureRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate));
                    combinedData.addAll(sensorDataTvocRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate));
                    combinedData.addAll(sensorDataAmbientNoiseRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate));
                    combinedData.addAll(sensorDataHumidityRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate));
                    combinedData.addAll(sensorDataPm2_5MassConcentrationRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate));
                    response.put("sensorType", "ALL");
                    break;
                case TEMPERATURE:
                    combinedData.addAll(sensorDataTemperatureRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate));
                    response.put("sensorType", "TEMPERATURE");
                    break;
                case TVOC:
                    combinedData.addAll(sensorDataTvocRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate));
                    response.put("sensorType", "TVOC");
                    break;
                case AMBIENT_NOISE:
                    combinedData.addAll(sensorDataAmbientNoiseRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate));
                    response.put("sensorType", "AMBIENTNOISE");
                    break;
                case HUMIDITY:
                    combinedData.addAll(sensorDataHumidityRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate));
                    response.put("sensorType", "HUMIDITY");
                    break;
                case PM2_5_MASS_CONCENTRATION:
                    combinedData.addAll(sensorDataPm2_5MassConcentrationRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate));
                    response.put("sensorType", "PM2_5MASSCONCENTRATION");
                    break;
                default:
                    throw new CustomException(ErrorCode.INVALID_SENSOR_TYPE);
            }
        }

        // 최종적으로 가져온 데이터를 timestamp 순서대로 정렬
        combinedData.sort(order == SortOrder.ASC ?
                Comparator.comparing(SensorDataDTO::getTimestamp) :
                Comparator.comparing(SensorDataDTO::getTimestamp).reversed());

        // 페이징 처리
        int start = page * size;
        int end = Math.min(start + size, combinedData.size());
        List<SensorDataDTO> paginatedData = combinedData.subList(start, end);

        // 페이징된 데이터와 메타 정보를 response에 추가
        response.put("data", paginatedData);
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalElements", combinedData.size());
        response.put("totalPages", (combinedData.size() + size - 1) / size);

        return response;
    }


    public Map<String, Object> getSensorDataBySensorId(
            SensorType sensorType,
            String sensorId,
            SortBy sortBy,
            SortOrder order,
            int page,
            int size
    ) {
        // 정렬 방향 설정
        Sort.Direction sortDirection = order == SortOrder.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy.getFieldName());

        // Pageable 객체 생성 (페이징 및 정렬)
        Pageable pageable = PageRequest.of(page, size, sort);

        // 응답을 위한 Map 초기화
        Map<String, Object> response = new HashMap<>();
        Page<?> sensorDataPage;

        // 센서 타입에 따라 적절한 리포지토리 호출 및 데이터 변환
        switch (sensorType) {
            case TEMPERATURE:
                sensorDataPage = sensorDataTemperatureRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "Temperature");
                break;
            case TVOC:
                sensorDataPage = sensorDataTvocRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "TVOC");
                break;
            case AMBIENT_NOISE:
                sensorDataPage = sensorDataAmbientNoiseRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "AmbientNoise");
                break;
            case IAQ_INDEX:
                sensorDataPage = sensorDataIaqIndexRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "IAQIndex");
                break;
            case AQM_SCORES:
                sensorDataPage = sensorDataAqmScoresRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "AQMScores");
                break;
            case HUMIDITY:
                sensorDataPage = sensorDataHumidityRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "Humidity");
                break;
            case USB_POWERED:
                sensorDataPage = sensorDataUsbPoweredRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "UsbPowered");
                break;
            case BUTTON_PRESSED:
                sensorDataPage = sensorDataButtonPressedRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "ButtonPressed");
                break;
            case WATER_DETECTION:
                sensorDataPage = sensorDataWaterDetectionRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "WaterDetection");
                break;
            case PM2_5_MASS_CONCENTRATION:
                sensorDataPage = sensorDataPm2_5MassConcentrationRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "PM2_5MassConcentration");
                break;
            default:
                throw new CustomException(ErrorCode.INVALID_SENSOR_TYPE);
        }

        response.put("data", convertToDTO(sensorDataPage.getContent()));
        return response;
    }

    public Map<String, Object> getSensorDataByBuildingAndName(
            SensorType sensorType,
            String building,
            String name,
            SortBy sortBy,
            SortOrder order,
            int page,
            int size
    ) {

        String sensorId = classroomRepository.findSensorIdByBuildingAndName(building, name);
        if (sensorId == null) {
            throw new CustomException(ErrorCode.SENSOR_NOT_FOUND);
        }

        return getSensorDataBySensorId(sensorType, sensorId, sortBy, order, page, size);
    }

    public Map<String, Object> getRecentSensorData(String sensorId) {
        Map<String, Object> response = new HashMap<>();

        // 각 SensorType을 반복
        for (SensorType sensorType : SensorType.values()) {
            switch (sensorType) {
//                case ALL:
//                    break;
                case TEMPERATURE:
                    SensorDataTemperature recentTemperature = sensorDataTemperatureRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentTemperature != null) {
                        response.put("Temperature", convertToDTO(recentTemperature));
                    }
                    break;
                case TVOC:
                    SensorDataTvoc recentTvoc = sensorDataTvocRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentTvoc != null) {
                        response.put("TVOC", convertToDTO(recentTvoc));
                    }
                    break;
                case AMBIENT_NOISE:
                    SensorDataAmbientNoise recentAmbientNoise = sensorDataAmbientNoiseRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentAmbientNoise != null) {
                        response.put("AmbientNoise", convertToDTO(recentAmbientNoise));
                    }
                    break;
                case IAQ_INDEX:
                    SensorDataIaqIndex recentIaqIndex = sensorDataIaqIndexRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentIaqIndex != null) {
                        response.put("IAQIndex", convertToDTO(recentIaqIndex));
                    }
                    break;
                case AQM_SCORES:
                    SensorDataAqmScores recentAqmScores = sensorDataAqmScoresRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentAqmScores != null) {
                        response.put("AQMScores", convertToDTO(recentAqmScores));
                    }
                    break;
                case HUMIDITY:
                    SensorDataHumidity recentHumidity = sensorDataHumidityRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentHumidity != null) {
                        response.put("Humidity", convertToDTO(recentHumidity));
                    }
                    break;
                case USB_POWERED:
                    SensorDataUsbPowered recentUsbPowered = sensorDataUsbPoweredRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentUsbPowered != null) {
                        response.put("UsbPowered", convertToDTO(recentUsbPowered));
                    }
                    break;
                case BUTTON_PRESSED:
                    SensorDataButtonPressed recentButtonPressed = sensorDataButtonPressedRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentButtonPressed != null) {
                        response.put("ButtonPressed", convertToDTO(recentButtonPressed));
                    }
                    break;
                case WATER_DETECTION:
                    SensorDataWaterDetection recentWaterDetection = sensorDataWaterDetectionRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentWaterDetection != null) {
                        response.put("WaterDetection", convertToDTO(recentWaterDetection));
                    }
                    break;
                case PM2_5_MASS_CONCENTRATION:
                    SensorDataPm2_5MassConcentration recentPm2_5 = sensorDataPm2_5MassConcentrationRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentPm2_5 != null) {
                        response.put("PM2_5MassConcentration", convertToDTO(recentPm2_5));
                    }
                    break;
                default:
                    throw new CustomException(ErrorCode.INVALID_SENSOR_TYPE);
            }
        }

        return response;
    }

    public List<SensorDataDTO> getAbnormalValuesOverTheLastHour() {
        LocalDateTime oneHourAgo = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusHours(1);

        List<SensorDataDTO> temperatureAnomalies = sensorDataTemperatureRepository.findAllByTimestampAfter(oneHourAgo).stream()
                .map(data -> {
                    String level = sensorDataChecker.checkTemperature(data.getValue());
                    data.setLevel(level); // level 값 설정
                    return data;
                })
                .filter(data -> "RED".equals(data.getLevel()) || "ORANGE".equals(data.getLevel()) || "YELLOW".equals(data.getLevel()))
                .collect(Collectors.toList());

        List<SensorDataDTO> humidityAnomalies = sensorDataHumidityRepository.findAllByTimestampAfter(oneHourAgo).stream()
                .map(data -> {
                    String level = sensorDataChecker.checkHumidity(data.getValue());
                    data.setLevel(level); // level 값 설정
                    return data;
                })
                .filter(data -> "RED".equals(data.getLevel()) || "ORANGE".equals(data.getLevel()) || "YELLOW".equals(data.getLevel()))
                .collect(Collectors.toList());

        List<SensorDataDTO> tvocAnomalies = sensorDataTvocRepository.findAllByTimestampAfter(oneHourAgo).stream()
                .map(data -> {
                    String level = sensorDataChecker.checkTvoc(data.getValue());
                    data.setLevel(level); // level 값 설정
                    return data;
                })
                .filter(data -> "RED".equals(data.getLevel()) || "ORANGE".equals(data.getLevel()) || "YELLOW".equals(data.getLevel()))
                .collect(Collectors.toList());

        List<SensorDataDTO> pm25Anomalies = sensorDataPm2_5MassConcentrationRepository.findAllByTimestampAfter(oneHourAgo).stream()
                .map(data -> {
                    String level = sensorDataChecker.checkPm25(data.getValue());
                    data.setLevel(level); // level 값 설정
                    return data;
                })
                .filter(data -> "RED".equals(data.getLevel()) || "ORANGE".equals(data.getLevel()) || "YELLOW".equals(data.getLevel()))
                .collect(Collectors.toList());

        List<SensorDataDTO> noiseAnomalies = sensorDataAmbientNoiseRepository.findAllByTimestampAfter(oneHourAgo).stream()
                .map(data -> {
                    String level = sensorDataChecker.checkNoise(data.getValue());
                    data.setLevel(level); // level 값 설정
                    return data;
                })
                .filter(data -> "RED".equals(data.getLevel()) || "ORANGE".equals(data.getLevel()) || "YELLOW".equals(data.getLevel()))
                .collect(Collectors.toList());

        List<SensorDataDTO> anomalies = new ArrayList<>();
        anomalies.addAll(temperatureAnomalies);
        anomalies.addAll(humidityAnomalies);
        anomalies.addAll(tvocAnomalies);
        anomalies.addAll(pm25Anomalies);
        anomalies.addAll(noiseAnomalies);

        anomalies.sort(Comparator.comparing(SensorDataDTO::getTimestamp).reversed());

        return anomalies;
    }

    public Map<String, Object> getRecentSensorDataByBuildingAndName(String building, String name) {

        String sensorId = classroomRepository.findSensorIdByBuildingAndName(building, name);

        if (sensorId == null) {
            throw new CustomException(ErrorCode.SENSOR_NOT_FOUND);
        }

        return getRecentSensorData(sensorId);
    }


    private List<?> convertToDTO(List<?> sensorDataEntities) {
        return sensorDataEntities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private Object convertToDTO(Object entity) {
        SensorDataDTO dto = new SensorDataDTO();
        AqmScoreDTO dto2 = new AqmScoreDTO();
        if (entity instanceof SensorDataTemperature) {
            SensorDataTemperature temperature = (SensorDataTemperature) entity;
            dto.setSensorId(temperature.getSensorId());
            dto.setTimestamp(temperature.getTimestamp());
            dto.setValue(temperature.getValue());
            return dto;
        } else if (entity instanceof SensorDataTvoc) {
            SensorDataTvoc tvoc = (SensorDataTvoc) entity;
            dto.setSensorId(tvoc.getSensorId());
            dto.setTimestamp(tvoc.getTimestamp());
            dto.setValue(tvoc.getValue());
            return dto;
        } else if (entity instanceof SensorDataAmbientNoise) {
            SensorDataAmbientNoise ambientNoise = (SensorDataAmbientNoise) entity;
            dto.setSensorId(ambientNoise.getSensorId());
            dto.setTimestamp(ambientNoise.getTimestamp());
            dto.setValue(ambientNoise.getValue());
            return dto;
        } else if (entity instanceof SensorDataIaqIndex) {
            SensorDataIaqIndex iaqIndex = (SensorDataIaqIndex) entity;
            dto.setSensorId(iaqIndex.getSensorId());
            dto.setTimestamp(iaqIndex.getTimestamp());
            dto.setValue(iaqIndex.getValue());
            return dto;
        } else if (entity instanceof SensorDataAqmScores) {
            SensorDataAqmScores aqmScores = (SensorDataAqmScores) entity;
            dto2.setSensorId(aqmScores.getSensorId());
            dto2.setTimestamp(aqmScores.getTimestamp());

            String jsonData = aqmScores.getAqmScores();
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(jsonData);

                dto2.setTemperatureStatus(jsonNode.get("temperature").asText());
                dto2.setHumidityStatus(jsonNode.get("humidity").asText());
                dto2.setTvocStatus(jsonNode.get("tvoc").asText());
                dto2.setPm25Status(jsonNode.get("PM2_5MassConcentration").asText());
                dto2.setAmbientNoiseStatus(jsonNode.get("ambientNoise").asText());
                dto2.setCo2Status(jsonNode.get("CO2").asText());
                return dto2;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (entity instanceof SensorDataHumidity) {
            SensorDataHumidity humidity = (SensorDataHumidity) entity;
            dto.setSensorId(humidity.getSensorId());
            dto.setTimestamp(humidity.getTimestamp());
            dto.setValue(humidity.getValue());
            return dto;
        } else if (entity instanceof SensorDataUsbPowered) {
            SensorDataUsbPowered usbPowered = (SensorDataUsbPowered) entity;
            dto.setSensorId(usbPowered.getSensorId());
            dto.setTimestamp(usbPowered.getTimestamp());
            dto.setValue(usbPowered.getValue() ? 1.0 : 0.0);
            return dto;
        } else if (entity instanceof SensorDataButtonPressed) {
            SensorDataButtonPressed buttonPressed = (SensorDataButtonPressed) entity;
            dto.setSensorId(buttonPressed.getSensorId());
            dto.setTimestamp(buttonPressed.getTimestamp());
            dto.setValue(buttonPressed.getValue() ? 1.0 : 0.0);
            return dto;
        } else if (entity instanceof SensorDataWaterDetection) {
            SensorDataWaterDetection waterDetection = (SensorDataWaterDetection) entity;
            dto.setSensorId(waterDetection.getSensorId());
            dto.setTimestamp(waterDetection.getTimestamp());
            dto.setValue(waterDetection.getValue() ? 1.0 : 0.0);
            return dto;
        } else if (entity instanceof SensorDataPm2_5MassConcentration) {
            SensorDataPm2_5MassConcentration pm2_5 = (SensorDataPm2_5MassConcentration) entity;
            dto.setSensorId(pm2_5.getSensorId());
            dto.setTimestamp(pm2_5.getTimestamp());
            dto.setValue(pm2_5.getValue());
            return dto;
        }

        return null;

    }

}
