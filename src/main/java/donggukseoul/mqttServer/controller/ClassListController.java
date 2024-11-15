package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.dto.ClassroomCreateDTO;
import donggukseoul.mqttServer.dto.ClassroomDTO;
import donggukseoul.mqttServer.dto.FavoriteClassroomDTO;
import donggukseoul.mqttServer.service.ClassroomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/classrooms")
@RequiredArgsConstructor
public class ClassListController {

    private final ClassroomService classroomService;

    @GetMapping
    public ResponseEntity<Stream<ClassroomDTO>> getClassrooms() {
        Stream<ClassroomDTO> classrooms = classroomService.getClassrooms();
        return ResponseEntity.ok(classrooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomDTO> getClassroomById(@PathVariable Long id) {
        return ResponseEntity.ok(classroomService.getClassroomById(id));
    }

    // ID 없이 강의실 생성
    @PostMapping
    public ResponseEntity<ClassroomDTO> createClassroom(@RequestBody ClassroomCreateDTO classroomCreateDto) {
        ClassroomDTO createdClassroom = classroomService.createClassroom(classroomCreateDto);
        return ResponseEntity.ok(createdClassroom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassroomDTO> updateClassroomSensor(
            @PathVariable Long id,
            @RequestParam String sensorId
    ) {
        return ResponseEntity.ok(classroomService.updateClassroomSensor(id, sensorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long id) {
        classroomService.deleteClassroom(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/favorite")
    public String toggleFavoriteClassroom(
            @RequestParam String building,
            @RequestParam String name,
            HttpServletRequest request) {
        return classroomService.toggleFavoriteClassroom(building, name, request);
    }

    @GetMapping("/myFavorites")
    public ResponseEntity<List<FavoriteClassroomDTO>> getClassroomsWithOptions(
            @RequestParam(required = false) String building,
            @RequestParam(defaultValue = "true") boolean favoriteFirst,
            @RequestParam(defaultValue = "asc") String orderDirection,
            HttpServletRequest request) {

        List<FavoriteClassroomDTO> classrooms = classroomService.getClassroomsWithOptions(building, favoriteFirst, orderDirection, request);
        return ResponseEntity.ok(classrooms);
    }
}
