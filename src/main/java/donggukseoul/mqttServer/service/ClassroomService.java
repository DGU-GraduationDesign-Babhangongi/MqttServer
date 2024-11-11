package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.dto.ClassroomCreateDTO;
import donggukseoul.mqttServer.dto.ClassroomDTO;
import donggukseoul.mqttServer.entity.Classroom;
import donggukseoul.mqttServer.entity.SensorInstallationLog;
import donggukseoul.mqttServer.entity.User;
import donggukseoul.mqttServer.jwt.JWTUtil;
import donggukseoul.mqttServer.repository.ClassroomRepository;
import donggukseoul.mqttServer.repository.SensorInstallationLogRepository;
import donggukseoul.mqttServer.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final SensorInstallationLogRepository sensorInstallationLogRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;



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

//    @Transactional
//    public void addClassroomToFavorites(Long classroomId, Long userId) {
//        Classroom classroom = classroomRepository.findById(classroomId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid classroom ID"));
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
//
//        classroom.addFavoritedByUser(user);
//        classroomRepository.save(classroom);
//    }
//
//    @Transactional
//    public void removeClassroomFromFavorites(Long classroomId, Long userId) {
//        Classroom classroom = classroomRepository.findById(classroomId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid classroom ID"));
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
//
//        classroom.removeFavoritedByUser(user);
//        classroomRepository.save(classroom);
//    }

    @Transactional
    public String toggleFavoriteClassroom(Long classroomId, HttpServletRequest request) {
        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("token null");
//            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return null;
        }

        System.out.println("authorization now");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");
//            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return null;
        }

        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);


        Long userId = userRepository.findByUsername(username).getId();


        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid classroom ID"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

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
                .build();
    }
}
