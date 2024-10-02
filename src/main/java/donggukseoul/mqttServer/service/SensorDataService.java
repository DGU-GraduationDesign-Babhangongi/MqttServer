package donggukseoul.mqttServer.service;


import donggukseoul.mqttServer.dto.SensorDataDTO;
import donggukseoul.mqttServer.entity.*;
import donggukseoul.mqttServer.enums.SensorType;
import donggukseoul.mqttServer.enums.SortBy;
import donggukseoul.mqttServer.enums.SortOrder;
import donggukseoul.mqttServer.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorDataService {
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


    // DTO 변환 메서드
    private List<SensorDataDTO> convertToDTO(List<?> sensorDataEntities) {
        return sensorDataEntities.stream().map(entity -> {
            SensorDataDTO dto = new SensorDataDTO();
            if (entity instanceof SensorDataTemperature) {
                SensorDataTemperature temperature = (SensorDataTemperature) entity;
                dto.setSensorId(temperature.getSensorId());
                dto.setTimestamp(temperature.getTimestamp());
                dto.setValue(temperature.getValue());
            } else if (entity instanceof SensorDataTvoc) {
                SensorDataTvoc tvoc = (SensorDataTvoc) entity;
                dto.setSensorId(tvoc.getSensorId());
                dto.setTimestamp(tvoc.getTimestamp());
                dto.setValue(tvoc.getValue());
            } else if (entity instanceof SensorDataAmbientNoise) {
                SensorDataAmbientNoise ambientNoise = (SensorDataAmbientNoise) entity;
                dto.setSensorId(ambientNoise.getSensorId());
                dto.setTimestamp(ambientNoise.getTimestamp());
                dto.setValue(ambientNoise.getValue());
            } else if (entity instanceof SensorDataIaqIndex) {
                SensorDataIaqIndex iaqIndex = (SensorDataIaqIndex) entity;
                dto.setSensorId(iaqIndex.getSensorId());
                dto.setTimestamp(iaqIndex.getTimestamp());
                dto.setValue(iaqIndex.getValue());
            } else if (entity instanceof SensorDataAqmScores) {
                SensorDataAqmScores aqmScores = (SensorDataAqmScores) entity;
                dto.setSensorId(aqmScores.getSensorId());
                dto.setTimestamp(aqmScores.getTimestamp());
                dto.setData(aqmScores.getAqmScores());
            } else if (entity instanceof SensorDataHumidity) {
                SensorDataHumidity humidity = (SensorDataHumidity) entity;
                dto.setSensorId(humidity.getSensorId());
                dto.setTimestamp(humidity.getTimestamp());
                dto.setValue(humidity.getValue());
            } else if (entity instanceof SensorDataUsbPowered) {
                SensorDataUsbPowered usbPowered = (SensorDataUsbPowered) entity;
                dto.setSensorId(usbPowered.getSensorId());
                dto.setTimestamp(usbPowered.getTimestamp());
                dto.setValue(usbPowered.getValue() ? 1.0 : 0.0);
            } else if (entity instanceof SensorDataButtonPressed) {
                SensorDataButtonPressed buttonPressed = (SensorDataButtonPressed) entity;
                dto.setSensorId(buttonPressed.getSensorId());
                dto.setTimestamp(buttonPressed.getTimestamp());
                dto.setValue(buttonPressed.getValue() ? 1.0 : 0.0);
            } else if (entity instanceof SensorDataWaterDetection) {
                SensorDataWaterDetection waterDetection = (SensorDataWaterDetection) entity;
                dto.setSensorId(waterDetection.getSensorId());
                dto.setTimestamp(waterDetection.getTimestamp());
                dto.setValue(waterDetection.getValue() ? 1.0 : 0.0);
            } else if (entity instanceof SensorDataPm2_5MassConcentration) {
                SensorDataPm2_5MassConcentration pm25 = (SensorDataPm2_5MassConcentration) entity;
                dto.setSensorId(pm25.getSensorId());
                dto.setTimestamp(pm25.getTimestamp());
                dto.setValue(pm25.getValue());
            }
            return dto;
        }).collect(Collectors.toList());
    }
}
