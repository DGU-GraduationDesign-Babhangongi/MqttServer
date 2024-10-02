package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.dto.ClassroomCreateDTO;
import donggukseoul.mqttServer.dto.ClassroomDTO;
import donggukseoul.mqttServer.entity.Classroom;
import donggukseoul.mqttServer.entity.SensorInstallationLog;
import donggukseoul.mqttServer.repository.ClassroomRepository;
import donggukseoul.mqttServer.repository.SensorInstallationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final SensorInstallationLogRepository sensorInstallationLogRepository;

//    public Page<ClassroomDTO> getClassrooms(Pageable pageable) {
//        return classroomRepository.findAll(pageable).map(this::convertToDto);
//    }
    public Stream<ClassroomDTO> getClassrooms() {
        Stream<ClassroomDTO> classroom = classroomRepository.findAll().stream().map(this::convertToDto);
        return classroom;
    }
    public ClassroomDTO getClassroomById(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid classroom ID"));
        return convertToDto(classroom);
    }

    // ClassroomCreateDto로 ID 없이 강의실 생성
    public ClassroomDTO createClassroom(ClassroomCreateDTO classroomCreateDto) {
        Classroom classroom = Classroom.builder()
                .name(classroomCreateDto.getName())
                .floor(classroomCreateDto.getFloor())
                .building(classroomCreateDto.getBuilding())
                .sensorId(classroomCreateDto.getSensorId())  // sensorId는 null일 수 있음
                .build();
        classroomRepository.save(classroom);

        if (classroomCreateDto.getSensorId() != null) {
            logSensorInstallation(classroom, classroomCreateDto.getSensorId());
        }

        return convertToDto(classroom);
    }

    public ClassroomDTO updateClassroomSensor(Long id, String sensorId) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid classroom ID"));
        String previousSensorId = classroom.getSensorId();
        classroom.setSensorId(sensorId);
        classroomRepository.save(classroom);

        if (sensorId != null && !sensorId.equals(previousSensorId)) {
            logSensorInstallation(classroom, sensorId);
        }

        return convertToDto(classroom);
    }

    public void deleteClassroom(Long id) {
        if (!classroomRepository.existsById(id)) {
            throw new IllegalArgumentException("Classroom ID does not exist");
        }
        classroomRepository.deleteById(id);
    }

    private void logSensorInstallation(Classroom classroom, String sensorId) {
        SensorInstallationLog log = SensorInstallationLog.builder()
                .classroom(classroom)
                .sensorId(sensorId)
                .timestamp(LocalDateTime.now())
                .build();
        sensorInstallationLogRepository.save(log);
    }

    private ClassroomDTO convertToDto(Classroom classroom) {
        return ClassroomDTO.builder()
                .id(classroom.getId())
                .name(classroom.getName())
                .floor(classroom.getFloor())
                .building(classroom.getBuilding())
                .sensorId(classroom.getSensorId())
                .build();
    }
}
