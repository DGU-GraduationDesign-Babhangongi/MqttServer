package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.dto.ClassroomCreateDTO;
import donggukseoul.mqttServer.dto.ClassroomDTO;
import donggukseoul.mqttServer.dto.FavoriteClassroomDTO;
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

    public Stream<ClassroomDTO> getClassrooms() {
        Stream<ClassroomDTO> classroom = classroomRepository.findAll().stream().map(this::convertToDto);
        return classroom;
    }
    public ClassroomDTO getClassroomById(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid classroom ID"));
        return convertToDto(classroom);
    }

    public List<FavoriteClassroomDTO> getClassroomsWithOptions(String building, boolean favoriteFirst, String orderDirection, HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token");
        }

        // Bearer 부분 제거 후 토큰 획득
        String token = authorization.split(" ")[1];

        // 토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            throw new IllegalArgumentException("Token expired");
        }

        // 토큰에서 username 획득
        String username = jwtUtil.getUsername(token);

        // 사용자 정보 조회
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // building 값에 따라 전체 또는 특정 건물의 강의실 목록 조회
        List<Classroom> classrooms = (building == null || building.isEmpty())
                ? classroomRepository.findAll()
                : classroomRepository.findByBuilding(building);

        // 즐겨찾기한 강의실 목록과 즐겨찾기하지 않은 강의실 목록으로 분리
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

        // favoriteFirst에 따라 리스트 합치기
        if (favoriteFirst) {
            favoritedList.addAll(nonFavoritedList);
            return favoritedList;
        } else {
            return Stream.concat(favoritedList.stream(), nonFavoritedList.stream())
                    .sorted(getNameComparator(orderDirection)) // 이름 순서 정렬
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
                .isFavorited(isFavorited) // 즐겨찾기 여부 설정
                .build();
    }

    public boolean isFavoriteClassroom(String building, String name, HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token");
        }

        // Bearer 부분 제거 후 토큰 획득
        String token = authorization.split(" ")[1];

        // 토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            throw new IllegalArgumentException("Token expired");
        }

        // 토큰에서 username 획득
        String username = jwtUtil.getUsername(token);

        // 사용자 정보 조회
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // 강의실 정보 조회
        Classroom classroom = classroomRepository.findByBuildingAndName(building, name)
                .orElseThrow(() -> new IllegalArgumentException("Classroom not found for given building and name"));

        // 즐겨찾기 여부 반환
        return user.getFavoriteClassrooms().contains(classroom);
    }


    // ClassroomCreateDto로 ID 없이 강의실 생성
    public ClassroomDTO createClassroom(ClassroomCreateDTO classroomCreateDto) {
        Classroom classroom = Classroom.builder()
                .name(classroomCreateDto.getName())
                .floor(classroomCreateDto.getFloor())
                .building(classroomCreateDto.getBuilding())
                .sensorId(classroomCreateDto.getSensorId())  // sensorId는 null일 수 있음
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

    private void logSensorInstallation(Classroom classroom, String sensorId) {
        SensorInstallationLog log = SensorInstallationLog.builder()
                .classroom(classroom)
                .sensorId(sensorId)
                .timestamp(LocalDateTime.now())
                .build();
        sensorInstallationLogRepository.save(log);
    }


    @Transactional
    public String toggleFavoriteClassroom(String building, String name, HttpServletRequest request) {
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


        Long classroomId = classroomRepository.findByBuildingAndName(building, name)
                .orElseThrow(() -> new IllegalArgumentException("Classroom not found for given building and name"))
                .getId();
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
                .sensorType(classroom.getSensorType())
                .build();
    }
}
