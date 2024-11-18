package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.dto.DeviceDTO;
import donggukseoul.mqttServer.entity.Classroom;
import donggukseoul.mqttServer.entity.Device;
import donggukseoul.mqttServer.repository.ClassroomRepository;
import donggukseoul.mqttServer.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final ClassroomRepository classroomRepository;

    public List<DeviceDTO> getDevicesByClassroom(String building, String name) {
        Classroom classroom = classroomRepository.findByBuildingAndName(building, name)
                .orElseThrow(() -> new IllegalArgumentException("Classroom not found for given building and name"));
        return deviceRepository.findByClassroom(classroom).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO toggleDeviceStatus(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid device ID"));
        device.setStatus(!device.isStatus());
        return convertToDTO(deviceRepository.save(device));
    }

    public DeviceDTO addDeviceToClassroom(String building, String name, String type) {
        Classroom classroom = classroomRepository.findByBuildingAndName(building, name)
                .orElseThrow(() -> new IllegalArgumentException("Classroom not found for given building and name"));
        Device device = Device.builder()
                .type(type)
                .status(false)
                .classroom(classroom)
                .build();
        return convertToDTO(deviceRepository.save(device));
    }

    public void removeDevice(Long deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new IllegalArgumentException("Device ID does not exist");
        }
        deviceRepository.deleteById(deviceId);
    }

    private DeviceDTO convertToDTO(Device device) {
        return new DeviceDTO(device.getId(), device.getType(), device.isStatus());
    }
}