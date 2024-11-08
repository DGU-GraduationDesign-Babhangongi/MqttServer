package donggukseoul.mqttServer.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import donggukseoul.mqttServer.dto.AqmScoreDTO;
import donggukseoul.mqttServer.dto.SensorDataDTO;
import donggukseoul.mqttServer.entity.*;
import donggukseoul.mqttServer.enums.SensorType;
import donggukseoul.mqttServer.enums.SortBy;
import donggukseoul.mqttServer.enums.SortOrder;
import donggukseoul.mqttServer.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class SensorDataService {
    private final ClassroomRepository classroomRepository;

//    private final SensorDataRepository sensorDataRepository;

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
//            case ALL:
//                List<Object> combinedData = new ArrayList<>();
//
//                combinedData.addAll(sensorDataTemperatureRepository.findAll(pageable).getContent());
//                combinedData.addAll(sensorDataTvocRepository.findAll(pageable).getContent());
//                combinedData.addAll(sensorDataAmbientNoiseRepository.findAll(pageable).getContent());
//                combinedData.addAll(sensorDataIaqIndexRepository.findAll(pageable).getContent());
//                combinedData.addAll(sensorDataAqmScoresRepository.findAll(pageable).getContent());
//                combinedData.addAll(sensorDataHumidityRepository.findAll(pageable).getContent());
//                combinedData.addAll(sensorDataUsbPoweredRepository.findAll(pageable).getContent());
//                combinedData.addAll(sensorDataButtonPressedRepository.findAll(pageable).getContent());
//                combinedData.addAll(sensorDataWaterDetectionRepository.findAll(pageable).getContent());
//                combinedData.addAll(sensorDataPm2_5MassConcentrationRepository.findAll(pageable).getContent());
//
//                combinedData = combinedData.stream()
//                        .sorted((data1, data2) -> {
//                            LocalDateTime timestamp1 = getTimestampField(data1);
//                            LocalDateTime timestamp2 = getTimestampField(data2);
//                            return sortDirection == Sort.Direction.ASC ? timestamp1.compareTo(timestamp2) : timestamp2.compareTo(timestamp1);
//                        })
//                        .skip((long) page * size) // 페이지 시작점 건너뛰기
//                        .limit(size) // 페이지 크기만큼 제한
//                        .collect(Collectors.toList());
//
//                response.put("sensorType", "ALL");
//                response.put("data", combinedData.stream().map(this::convertToDTO).collect(Collectors.toList()));
//                break;
            case TEMPERATURE:
                sensorDataPage = sensorDataTemperatureRepository.findAll(pageable);
                response.put("sensorType", "Temperature");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case TVOC:
                sensorDataPage = sensorDataTvocRepository.findAll(pageable);
                response.put("sensorType", "TVOC");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case AMBIENTNOISE:
                sensorDataPage = sensorDataAmbientNoiseRepository.findAll(pageable);
                response.put("sensorType", "AmbientNoise");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case IAQINDEX:
                sensorDataPage = sensorDataIaqIndexRepository.findAll(pageable);
                response.put("sensorType", "IAQIndex");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case AQMSCORES:
                sensorDataPage = sensorDataAqmScoresRepository.findAll(pageable);
                response.put("sensorType", "AQMScores");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case HUMIDITY:
                sensorDataPage = sensorDataHumidityRepository.findAll(pageable);
                response.put("sensorType", "Humidity");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case USBPOWERED:
                sensorDataPage = sensorDataUsbPoweredRepository.findAll(pageable);
                response.put("sensorType", "UsbPowered");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case BUTTONPRESSED:
                sensorDataPage = sensorDataButtonPressedRepository.findAll(pageable);
                response.put("sensorType", "ButtonPressed");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case WATERDETECTION:
                sensorDataPage = sensorDataWaterDetectionRepository.findAll(pageable);
                response.put("sensorType", "WaterDetection");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case PM2_5MASSCONCENTRATION:
                sensorDataPage = sensorDataPm2_5MassConcentrationRepository.findAll(pageable);
                response.put("sensorType", "PM2_5MassConcentration");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            default:
                throw new IllegalArgumentException("Invalid sensor type: " + sensorType);
        }

        return response;
    }

    public Map<String, Object> getCombinedSensorData(List<SensorType> sensorTypes, String building, String name, LocalDateTime startDate, LocalDateTime endDate, SortOrder order, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        List<SensorDataDTO> combinedData = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, size);

        String sensorId = classroomRepository.findSensorIdByBuildingAndName(building, name);
        if (sensorId == null) {
            throw new IllegalArgumentException("해당 강의실에 대한 센서가 없습니다.");
        }

        for (SensorType sensorType : sensorTypes) {
            switch (sensorType) {
                case ALL:

                    combinedData.addAll(sensorDataTemperatureRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable).getContent());
                    combinedData.addAll(sensorDataTvocRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable).getContent());
                    combinedData.addAll(sensorDataAmbientNoiseRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable).getContent());
                    combinedData.addAll(sensorDataHumidityRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable).getContent());
                    combinedData.addAll(sensorDataPm2_5MassConcentrationRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable).getContent());
                    response.put("sensorType", "ALL");
                    break;
                case TEMPERATURE:
                    Page<SensorDataDTO> temperatureData = sensorDataTemperatureRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable);
                    combinedData.addAll(temperatureData.getContent());
                    response.put("sensorType", "TEMPERATURE");
                    break;
                case TVOC:
                    Page<SensorDataDTO> tvocData = sensorDataTvocRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable);
                    combinedData.addAll(tvocData.getContent());
                    response.put("sensorType", "TVOC");
                    break;
                case AMBIENTNOISE:
                    Page<SensorDataDTO> ambientNoiseData = sensorDataAmbientNoiseRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable);
                    combinedData.addAll(ambientNoiseData.getContent());
                    response.put("sensorType", "AMBIENTNOISE");
                    break;
                case HUMIDITY:
                    Page<SensorDataDTO> humidityData = sensorDataHumidityRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable);
                    combinedData.addAll(humidityData.getContent());
                    response.put("sensorType", "HUMIDITY");
                    break;
                case PM2_5MASSCONCENTRATION:
                    Page<SensorDataDTO> pm25Data = sensorDataPm2_5MassConcentrationRepository.findAllBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable);
                    combinedData.addAll(pm25Data.getContent());
                    response.put("sensorType", "PM2_5MASSCONCENTRATION");
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported sensor type: " + sensorType);
            }
        }

        // 최종적으로 가져온 데이터를 timestamp 순서대로 정렬
        if (order == SortOrder.ASC) {
            combinedData.sort(Comparator.comparing(SensorDataDTO::getTimestamp));
        } else {
            combinedData.sort(Comparator.comparing(SensorDataDTO::getTimestamp).reversed());
        }

        // 페이징 처리된 데이터와 메타 정보를 response에 추가
        response.put("data", combinedData);
        response.put("currentPage", page);
        response.put("pageSize", size);

        return response;
    }





    public Map<String, Object> getSensorDataBetweenDates(
            SensorType sensorType, String building, String name,
            LocalDateTime startDate, LocalDateTime endDate, SortBy sortBy, SortOrder order,
            int page, int size
    ) {
        // 먼저 ClassroomRepository를 통해 building과 name으로 sensorId를 조회합니다.
        String sensorId = classroomRepository.findSensorIdByBuildingAndName(building, name);
        if (sensorId == null) {
            throw new IllegalArgumentException("해당 강의실에 대한 센서가 없습니다.");
        }

        // 정렬 방향 설정 및 페이징
        Sort.Direction sortDirection = order == SortOrder.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy.name().toLowerCase());
        Pageable pageable = PageRequest.of(page, size, sort);

        // 응답 데이터 초기화
        Map<String, Object> response = new HashMap<>();
        Page<?> sensorDataPage;
//        List<SensorDataDTO> sensorDataPage;

        // 센서 타입에 따라 적절한 리포지토리에서 데이터를 조회합니다.
        switch (sensorType) {
//            case ALL:
//                List<SensorDataDTO> combinedData = new ArrayList<>();
//
//                combinedData.addAll(sensorDataTemperatureRepository.findAllBySensorIdAndTimestampBetween(sensorId,startDate,endDate));
//                combinedData.addAll(sensorDataTvocRepository.findAllBySensorIdAndTimestampBetween(sensorId,startDate,endDate));
//                combinedData.addAll(sensorDataAmbientNoiseRepository.findAllBySensorIdAndTimestampBetween(sensorId,startDate,endDate));
//                combinedData.addAll(sensorDataHumidityRepository.findAllBySensorIdAndTimestampBetween(sensorId,startDate,endDate));
//                combinedData.addAll(sensorDataPm2_5MassConcentrationRepository.findAllBySensorIdAndTimestampBetween(sensorId,startDate,endDate));
//
//                if (order == SortOrder.ASC) {
//                    combinedData.sort(Comparator.comparing(SensorDataDTO::getTimestamp));
//                } else {
//                    combinedData.sort(Comparator.comparing(SensorDataDTO::getTimestamp).reversed());
//                }
//
//                response.put("sensorType", "ALL");
//                response.put("data", combinedData);
//                break;
            case TEMPERATURE:
                sensorDataPage = sensorDataTemperatureRepository.findBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable);
                response.put("sensorType", "Temperature");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case TVOC:
                sensorDataPage = sensorDataTvocRepository.findBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable);
                response.put("sensorType", "TVOC");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case AMBIENTNOISE:
                sensorDataPage = sensorDataAmbientNoiseRepository.findBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable);
                response.put("sensorType", "AmbientNoise");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case IAQINDEX:
                sensorDataPage = sensorDataIaqIndexRepository.findBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable);
                response.put("sensorType", "IAQIndex");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case AQMSCORES:
                sensorDataPage = sensorDataAqmScoresRepository.findBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable);
                response.put("sensorType", "AQMScores");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case HUMIDITY:
                sensorDataPage = sensorDataHumidityRepository.findBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable);
                response.put("sensorType", "Humidity");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case USBPOWERED:
                sensorDataPage = sensorDataUsbPoweredRepository.findBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable);
                response.put("sensorType", "UsbPowered");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case BUTTONPRESSED:
                sensorDataPage = sensorDataButtonPressedRepository.findBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable);
                response.put("sensorType", "ButtonPressed");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case WATERDETECTION:
                sensorDataPage = sensorDataWaterDetectionRepository.findBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable);
                response.put("sensorType", "WaterDetection");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case PM2_5MASSCONCENTRATION:
                sensorDataPage = sensorDataPm2_5MassConcentrationRepository.findBySensorIdAndTimestampBetween(sensorId, startDate, endDate, pageable);
                response.put("sensorType", "PM2_5MassConcentration");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 센서 타입입니다: " + sensorType);
        }

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
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case TVOC:
                sensorDataPage = sensorDataTvocRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "TVOC");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case AMBIENTNOISE:
                sensorDataPage = sensorDataAmbientNoiseRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "AmbientNoise");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case IAQINDEX:
                sensorDataPage = sensorDataIaqIndexRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "IAQIndex");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case AQMSCORES:
                sensorDataPage = sensorDataAqmScoresRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "AQMScores");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case HUMIDITY:
                sensorDataPage = sensorDataHumidityRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "Humidity");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case USBPOWERED:
                sensorDataPage = sensorDataUsbPoweredRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "UsbPowered");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case BUTTONPRESSED:
                sensorDataPage = sensorDataButtonPressedRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "ButtonPressed");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case WATERDETECTION:
                sensorDataPage = sensorDataWaterDetectionRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "WaterDetection");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            case PM2_5MASSCONCENTRATION:
                sensorDataPage = sensorDataPm2_5MassConcentrationRepository.findBySensorId(sensorId, pageable);
                response.put("sensorType", "PM2_5MassConcentration");
                response.put("data", convertToDTO(sensorDataPage.getContent()));
                break;
            default:
                throw new IllegalArgumentException("Invalid sensor type: " + sensorType);
        }

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
            throw new IllegalArgumentException("No sensor found for the given classroom.");
        }

        Map<String, Object> response = getSensorDataBySensorId(sensorType, sensorId, sortBy, order, page, size);

        return response;
    }

    public Map<String, Object> getRecentSensorData(String sensorId) {
        Map<String, Object> response = new HashMap<>();

        // 각 SensorType을 반복
        for (SensorType sensorType : SensorType.values()) {
            switch (sensorType) {
                case ALL:
                    break;
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
                case AMBIENTNOISE:
                    SensorDataAmbientNoise recentAmbientNoise = sensorDataAmbientNoiseRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentAmbientNoise != null) {
                        response.put("AmbientNoise", convertToDTO(recentAmbientNoise));
                    }
                    break;
                case IAQINDEX:
                    SensorDataIaqIndex recentIaqIndex = sensorDataIaqIndexRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentIaqIndex != null) {
                        response.put("IAQIndex", convertToDTO(recentIaqIndex));
                    }
                    break;
                case AQMSCORES:
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
                case USBPOWERED:
                    SensorDataUsbPowered recentUsbPowered = sensorDataUsbPoweredRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentUsbPowered != null) {
                        response.put("UsbPowered", convertToDTO(recentUsbPowered));
                    }
                    break;
                case BUTTONPRESSED:
                    SensorDataButtonPressed recentButtonPressed = sensorDataButtonPressedRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentButtonPressed != null) {
                        response.put("ButtonPressed", convertToDTO(recentButtonPressed));
                    }
                    break;
                case WATERDETECTION:
                    SensorDataWaterDetection recentWaterDetection = sensorDataWaterDetectionRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentWaterDetection != null) {
                        response.put("WaterDetection", convertToDTO(recentWaterDetection));
                    }
                    break;
                case PM2_5MASSCONCENTRATION:
                    SensorDataPm2_5MassConcentration recentPm2_5 = sensorDataPm2_5MassConcentrationRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
                    if (recentPm2_5 != null) {
                        response.put("PM2_5MassConcentration", convertToDTO(recentPm2_5));
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sensor type: " + sensorType);
            }
        }

        return response;
    }

    public Map<String, Object> getRecentSensorDataByBuildingAndName(String building, String name) {

        String sensorId = classroomRepository.findSensorIdByBuildingAndName(building, name);
        Map<String, Object> response = getRecentSensorData(sensorId);

        return response;
    }



    // DTO 변환 메서드
    private List<?> convertToDTO(List<?> sensorDataEntities) {
        return sensorDataEntities.stream().map(entity -> {
            Object o = new Object();
            SensorDataDTO dto = new SensorDataDTO();
            AqmScoreDTO dto2 = new AqmScoreDTO();
            if (entity instanceof SensorDataTemperature) {
                SensorDataTemperature temperature = (SensorDataTemperature) entity;
                dto.setSensorId(temperature.getSensorId());
                dto.setTimestamp(temperature.getTimestamp());
                dto.setValue(temperature.getValue());
                o = dto;
            } else if (entity instanceof SensorDataTvoc) {
                SensorDataTvoc tvoc = (SensorDataTvoc) entity;
                dto.setSensorId(tvoc.getSensorId());
                dto.setTimestamp(tvoc.getTimestamp());
                dto.setValue(tvoc.getValue());
                o = dto;
            } else if (entity instanceof SensorDataAmbientNoise) {
                SensorDataAmbientNoise ambientNoise = (SensorDataAmbientNoise) entity;
                dto.setSensorId(ambientNoise.getSensorId());
                dto.setTimestamp(ambientNoise.getTimestamp());
                dto.setValue(ambientNoise.getValue());
                o = dto;
            } else if (entity instanceof SensorDataIaqIndex) {
                SensorDataIaqIndex iaqIndex = (SensorDataIaqIndex) entity;
                dto.setSensorId(iaqIndex.getSensorId());
                dto.setTimestamp(iaqIndex.getTimestamp());
                dto.setValue(iaqIndex.getValue());
                o = dto;
            } else if (entity instanceof SensorDataAqmScores) {
                SensorDataAqmScores aqmScores = (SensorDataAqmScores) entity;
                dto2.setSensorId(aqmScores.getSensorId());
                dto2.setTimestamp(aqmScores.getTimestamp());

                // JSON 데이터 파싱 및 DTO에 추가
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
                    o = dto2;

                } catch (Exception e) {
                    e.printStackTrace();
                    // 예외 처리 추가 (필요 시 로그 출력)
                }
            } else if (entity instanceof SensorDataHumidity) {
                SensorDataHumidity humidity = (SensorDataHumidity) entity;
                dto.setSensorId(humidity.getSensorId());
                dto.setTimestamp(humidity.getTimestamp());
                dto.setValue(humidity.getValue());
                o = dto;
            } else if (entity instanceof SensorDataUsbPowered) {
                SensorDataUsbPowered usbPowered = (SensorDataUsbPowered) entity;
                dto.setSensorId(usbPowered.getSensorId());
                dto.setTimestamp(usbPowered.getTimestamp());
                dto.setValue(usbPowered.getValue() ? 1.0 : 0.0);
                o = dto;
            } else if (entity instanceof SensorDataButtonPressed) {
                SensorDataButtonPressed buttonPressed = (SensorDataButtonPressed) entity;
                dto.setSensorId(buttonPressed.getSensorId());
                dto.setTimestamp(buttonPressed.getTimestamp());
                dto.setValue(buttonPressed.getValue() ? 1.0 : 0.0);
                o = dto;
            } else if (entity instanceof SensorDataWaterDetection) {
                SensorDataWaterDetection waterDetection = (SensorDataWaterDetection) entity;
                dto.setSensorId(waterDetection.getSensorId());
                dto.setTimestamp(waterDetection.getTimestamp());
                dto.setValue(waterDetection.getValue() ? 1.0 : 0.0);
                o = dto;
            } else if (entity instanceof SensorDataPm2_5MassConcentration) {
                SensorDataPm2_5MassConcentration pm25 = (SensorDataPm2_5MassConcentration) entity;
                dto.setSensorId(pm25.getSensorId());
                dto.setTimestamp(pm25.getTimestamp());
                dto.setValue(pm25.getValue());
                o = dto;
            }
            return o;
        }).collect(Collectors.toList());
    }

    private Object convertToDTO(Object entity) {
        Object o = new Object();
        SensorDataDTO dto = new SensorDataDTO();
        AqmScoreDTO dto2 = new AqmScoreDTO();
        if (entity instanceof SensorDataTemperature) {
            SensorDataTemperature temperature = (SensorDataTemperature) entity;
            dto.setSensorId(temperature.getSensorId());
            dto.setTimestamp(temperature.getTimestamp());
            dto.setValue(temperature.getValue());
            o = dto;
        } else if (entity instanceof SensorDataTvoc) {
            SensorDataTvoc tvoc = (SensorDataTvoc) entity;
            dto.setSensorId(tvoc.getSensorId());
            dto.setTimestamp(tvoc.getTimestamp());
            dto.setValue(tvoc.getValue());
            o = dto;
        } else if (entity instanceof SensorDataAmbientNoise) {
            SensorDataAmbientNoise ambientNoise = (SensorDataAmbientNoise) entity;
            dto.setSensorId(ambientNoise.getSensorId());
            dto.setTimestamp(ambientNoise.getTimestamp());
            dto.setValue(ambientNoise.getValue());
            o = dto;
        } else if (entity instanceof SensorDataIaqIndex) {
            SensorDataIaqIndex iaqIndex = (SensorDataIaqIndex) entity;
            dto.setSensorId(iaqIndex.getSensorId());
            dto.setTimestamp(iaqIndex.getTimestamp());
            dto.setValue(iaqIndex.getValue());
            o = dto;
        } else if (entity instanceof SensorDataAqmScores) {
            SensorDataAqmScores aqmScores = (SensorDataAqmScores) entity;
            dto2.setSensorId(aqmScores.getSensorId());
            dto2.setTimestamp(aqmScores.getTimestamp());

            // JSON 데이터 파싱 및 DTO에 추가
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
                o = dto2;

            } catch (Exception e) {
                e.printStackTrace();
                // 예외 처리 추가 (필요 시 로그 출력)
            }
        } else if (entity instanceof SensorDataHumidity) {
            SensorDataHumidity humidity = (SensorDataHumidity) entity;
            dto.setSensorId(humidity.getSensorId());
            dto.setTimestamp(humidity.getTimestamp());
            dto.setValue(humidity.getValue());
            o = dto;
        } else if (entity instanceof SensorDataUsbPowered) {
            SensorDataUsbPowered usbPowered = (SensorDataUsbPowered) entity;
            dto.setSensorId(usbPowered.getSensorId());
            dto.setTimestamp(usbPowered.getTimestamp());
            dto.setValue(usbPowered.getValue() ? 1.0 : 0.0); // Boolean 값을 Double로 변환
            o = dto;
        } else if (entity instanceof SensorDataButtonPressed) {
            SensorDataButtonPressed buttonPressed = (SensorDataButtonPressed) entity;
            dto.setSensorId(buttonPressed.getSensorId());
            dto.setTimestamp(buttonPressed.getTimestamp());
            dto.setValue(buttonPressed.getValue() ? 1.0 : 0.0); // Boolean 값을 Double로 변환
            o = dto;
        } else if (entity instanceof SensorDataWaterDetection) {
            SensorDataWaterDetection waterDetection = (SensorDataWaterDetection) entity;
            dto.setSensorId(waterDetection.getSensorId());
            dto.setTimestamp(waterDetection.getTimestamp());
            dto.setValue(waterDetection.getValue() ? 1.0 : 0.0); // Boolean 값을 Double로 변환
            o = dto;
        } else if (entity instanceof SensorDataPm2_5MassConcentration) {
            SensorDataPm2_5MassConcentration pm2_5 = (SensorDataPm2_5MassConcentration) entity;
            dto.setSensorId(pm2_5.getSensorId());
            dto.setTimestamp(pm2_5.getTimestamp());
            dto.setValue(pm2_5.getValue());
            o = dto;
        }

        return o;
    }

    private LocalDateTime getTimestampField(Object entity) {
        if (entity instanceof SensorDataTemperature) {
            return ((SensorDataTemperature) entity).getTimestamp();
        } else if (entity instanceof SensorDataTvoc) {
            return ((SensorDataTvoc) entity).getTimestamp();
        } else if (entity instanceof SensorDataAmbientNoise) {
            return ((SensorDataAmbientNoise) entity).getTimestamp();
        } else if (entity instanceof SensorDataIaqIndex) {
            return ((SensorDataIaqIndex) entity).getTimestamp();
        } else if (entity instanceof SensorDataAqmScores) {
            return ((SensorDataAqmScores) entity).getTimestamp();
        } else if (entity instanceof SensorDataHumidity) {
            return ((SensorDataHumidity) entity).getTimestamp();
        } else if (entity instanceof SensorDataUsbPowered) {
            return ((SensorDataUsbPowered) entity).getTimestamp();
        } else if (entity instanceof SensorDataButtonPressed) {
            return ((SensorDataButtonPressed) entity).getTimestamp();
        } else if (entity instanceof SensorDataWaterDetection) {
            return ((SensorDataWaterDetection) entity).getTimestamp();
        } else if (entity instanceof SensorDataPm2_5MassConcentration) {
            return ((SensorDataPm2_5MassConcentration) entity).getTimestamp();
        }
        throw new IllegalArgumentException("Invalid entity type: " + entity.getClass().getSimpleName());
    }




}
