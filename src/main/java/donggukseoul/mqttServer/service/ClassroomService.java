package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.dto.ClassroomCreateDTO;
import donggukseoul.mqttServer.dto.ClassroomDTO;
import donggukseoul.mqttServer.dto.FavoriteClassroomDTO;
import donggukseoul.mqttServer.entity.Classroom;
import donggukseoul.mqttServer.entity.ClassroomDeletionLog;
import donggukseoul.mqttServer.entity.SensorInstallationLog;
import donggukseoul.mqttServer.entity.User;
import donggukseoul.mqttServer.exception.CustomException;
import donggukseoul.mqttServer.exception.ErrorCode;
import donggukseoul.mqttServer.jwt.JWTUtil;
import donggukseoul.mqttServer.repository.ClassroomDeletionLogRepository;
import donggukseoul.mqttServer.repository.ClassroomRepository;
import donggukseoul.mqttServer.repository.SensorInstallationLogRepository;
import donggukseoul.mqttServer.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final SensorInstallationLogRepository sensorInstallationLogRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final ClassroomDeletionLogRepository classroomDeletionLogRepository;
    private final TokenBlacklistService tokenBlacklistService;

    public Stream<ClassroomDTO> getClassrooms() {
        Stream<ClassroomDTO> classroom = classroomRepository.findAll().stream().map(this::convertToDto);
        return classroom;
    }
    public ClassroomDTO getClassroomById(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CLASSROOM_ID));
        return convertToDto(classroom);
    }

    public List<FavoriteClassroomDTO> getClassroomsWithOptions(String building, boolean favoriteFirst, String orderDirection, HttpServletRequest request) throws ServletException, IOException {

        String token = jwtUtil.validateToken(request, null, null, tokenBlacklistService);

        if (token == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String username = jwtUtil.getUsername(token);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        List<Classroom> classrooms = (building == null || building.isEmpty())
                ? classroomRepository.findAll()
                : classroomRepository.findByBuilding(building);

        List<FavoriteClassroomDTO> favoritedList = classrooms.stream()
                .filter(classroom -> user.getFavoriteClassrooms().contains(classroom))
                .map(classroom -> convertToFavoriteClassroomDto(classroom, true))
                .sorted(getNameComparator(orderDirection))
                .collect(Collectors.toList());

        List<FavoriteClassroomDTO> nonFavoritedList = classrooms.stream()
                .filter(classroom -> !user.getFavoriteClassrooms().contains(classroom))
                .map(classroom -> convertToFavoriteClassroomDto(classroom, false))
                .sorted(getNameComparator(orderDirection))
                .collect(Collectors.toList());

        if (favoriteFirst) {
            favoritedList.addAll(nonFavoritedList);
            return favoritedList;
        } else {
            return Stream.concat(favoritedList.stream(), nonFavoritedList.stream())
                    .sorted(getNameComparator(orderDirection))
                    .collect(Collectors.toList());
        }
    }

    private Comparator<FavoriteClassroomDTO> getNameComparator(String orderDirection) {
        Comparator<FavoriteClassroomDTO> nameComparator = Comparator.comparing(FavoriteClassroomDTO::getName);
        if ("desc".equalsIgnoreCase(orderDirection)) {
            return nameComparator.reversed();
        }
        return nameComparator;
    }

    private FavoriteClassroomDTO convertToFavoriteClassroomDto(Classroom classroom, boolean isFavorited) {
        return FavoriteClassroomDTO.builder()
                .id(classroom.getId())
                .name(classroom.getName())
                .floor(classroom.getFloor())
                .building(classroom.getBuilding())
                .sensorId(classroom.getSensorId())
                .sensorType(classroom.getSensorType())
                .isFavorited(isFavorited)
                .build();
    }

    public boolean isFavoriteClassroom(String building, String name, HttpServletRequest request) throws ServletException, IOException {

        String token = jwtUtil.validateToken(request, null, null, tokenBlacklistService);

        if (token == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String username = jwtUtil.getUsername(token);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Classroom classroom = classroomRepository.findByBuildingAndName(building, name)
                .orElseThrow(() -> new CustomException(ErrorCode.CLASSROOM_NOT_FOUND));

        return user.getFavoriteClassrooms().contains(classroom);
    }


    public ClassroomDTO createClassroom(ClassroomCreateDTO classroomCreateDto) {
        Classroom classroom = Classroom.builder()
                .name(classroomCreateDto.getName())
                .floor(classroomCreateDto.getFloor())
                .building(classroomCreateDto.getBuilding())
                .sensorId(classroomCreateDto.getSensorId())
                .sensorType(classroomCreateDto.getSensorType())
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

    public void deleteClassroomByName(String building, String name, String reason) {
        Classroom classroom = classroomRepository.findByBuildingAndName(building, name)
                .orElseThrow(() -> new IllegalArgumentException("Classroom not found for given building and name"));

        ClassroomDeletionLog deletionLog = ClassroomDeletionLog.builder()
                .classroomId(classroom.getId())
                .reason(reason)
                .timestamp(LocalDateTime.now())
                .build();
        classroomDeletionLogRepository.save(deletionLog);

        classroomRepository.delete(classroom);
    }

    private void logSensorInstallation(Classroom classroom, String sensorId) {
        SensorInstallationLog log = SensorInstallationLog.builder()
                .classroomId(classroom.getId())
                .sensorId(sensorId)
                .timestamp(LocalDateTime.now())
                .build();
        sensorInstallationLogRepository.save(log);
    }


    @Transactional
    public String toggleFavoriteClassroom(String building, String name, HttpServletRequest request) throws ServletException, IOException {

        String token = jwtUtil.validateToken(request, null, null, tokenBlacklistService);

        if (token == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String username = jwtUtil.getUsername(token);

        Long classroomId = classroomRepository.findByBuildingAndName(building, name)
                .orElseThrow(() -> new CustomException(ErrorCode.CLASSROOM_NOT_FOUND))
                .getId();
        Long userId = userRepository.findByUsername(username).getId();


        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CLASSROOM_ID));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER_ID));

        if (classroom.getFavoritedByUsers().contains(user)) {
            classroom.removeFavoritedByUser(user);
            classroomRepository.save(classroom);
            return "Classroom removed from favorites.";
        } else {
            classroom.addFavoritedByUser(user);
            classroomRepository.save(classroom);
            return "Classroom added to favorites.";
        }
    }


    private ClassroomDTO convertToDto(Classroom classroom) {
        return ClassroomDTO.builder()
                .id(classroom.getId())
                .name(classroom.getName())
                .floor(classroom.getFloor())
                .building(classroom.getBuilding())
                .sensorId(classroom.getSensorId())
                .sensorType(classroom.getSensorType())
                .build();
    }
}
