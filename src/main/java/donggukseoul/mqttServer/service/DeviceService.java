package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.dto.DeviceDTO;
import donggukseoul.mqttServer.entity.Classroom;
import donggukseoul.mqttServer.entity.Device;
import donggukseoul.mqttServer.entity.User;
import donggukseoul.mqttServer.exception.CustomException;
import donggukseoul.mqttServer.exception.ErrorCode;
import donggukseoul.mqttServer.repository.ClassroomRepository;
import donggukseoul.mqttServer.repository.DeviceRepository;
import donggukseoul.mqttServer.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final ClassroomRepository classroomRepository;

    private final UserRepository userRepository;

    private final EmailService emailService;

    public List<DeviceDTO> getDevicesByClassroom(String building, String name) {
        Classroom classroom = classroomRepository.findByBuildingAndName(building, name)
                .orElseThrow(() -> new CustomException(ErrorCode.CLASSROOM_NOT_FOUND));
        return deviceRepository.findByClassroom(classroom).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO toggleDeviceStatus(Long deviceId) throws MessagingException {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_DEVICE_ID));
        device.setStatus(!device.isStatus());
        DeviceDTO deviceDTO = convertToDTO(deviceRepository.save(device));

        if (device.isStatus()) {
            notifyUsers(device.getClassroom());
        }

        return deviceDTO;
    }

    public DeviceDTO addDeviceToClassroom(String building, String name, String type) {
        Classroom classroom = classroomRepository.findByBuildingAndName(building, name)
                .orElseThrow(() -> new CustomException(ErrorCode.CLASSROOM_NOT_FOUND));
        Device device = Device.builder()
                .type(type)
                .status(false)
                .classroom(classroom)
                .build();
        return convertToDTO(deviceRepository.save(device));
    }

    public void removeDevice(Long deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new CustomException(ErrorCode.INVALID_DEVICE_ID);
        }
        deviceRepository.deleteById(deviceId);
    }

    private DeviceDTO convertToDTO(Device device) {
        return new DeviceDTO(device.getId(), device.getType(), device.isStatus());
    }

    private void notifyUsers(Classroom classroom) throws MessagingException {
        List<User> users = userRepository.findByAreaOfResponsibility(classroom.getBuilding());
        for (User user : users) {
            if (user.isAlarmStatus()) {
                emailService.sendNotificationEmail(user.getEmail(), classroom.getName());
            }
        }
    }
}