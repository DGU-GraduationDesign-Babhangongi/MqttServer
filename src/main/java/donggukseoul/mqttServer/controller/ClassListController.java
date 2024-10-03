package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.dto.ClassroomCreateDTO;
import donggukseoul.mqttServer.dto.ClassroomDTO;
import donggukseoul.mqttServer.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
