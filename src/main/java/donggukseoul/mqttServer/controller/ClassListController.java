package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.dto.ClassroomCreateDTO;
import donggukseoul.mqttServer.dto.ClassroomDTO;
import donggukseoul.mqttServer.dto.FavoriteClassroomDTO;
import donggukseoul.mqttServer.service.ClassroomService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @DeleteMapping("/name")
    public ResponseEntity<Void> deleteClassroomByName(@RequestParam String building, @RequestParam String name, @RequestParam String reason) {
        classroomService.deleteClassroomByName(building, name, reason);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/favorite")
    public String toggleFavoriteClassroom(
            @RequestParam String building,
            @RequestParam String name,
            HttpServletRequest request) throws ServletException, IOException {
        return classroomService.toggleFavoriteClassroom(building, name, request);
    }

    @GetMapping("/myFavorites")
    public ResponseEntity<List<FavoriteClassroomDTO>> getClassroomsWithOptions(
            @RequestParam(required = false) String building,
            @RequestParam(defaultValue = "true") boolean favoriteFirst,
            @RequestParam(defaultValue = "asc") String orderDirection,
            HttpServletRequest request) throws ServletException, IOException {

        List<FavoriteClassroomDTO> classrooms = classroomService.getClassroomsWithOptions(building, favoriteFirst, orderDirection, request);
        return ResponseEntity.ok(classrooms);
    }

    @GetMapping("/favorite/status")
    public ResponseEntity<Boolean> isFavoriteClassroom(
            @RequestParam String building,
            @RequestParam String name,
            HttpServletRequest request) throws ServletException, IOException {
        boolean isFavorite = classroomService.isFavoriteClassroom(building, name, request);
        return ResponseEntity.ok(isFavorite);
    }
}
