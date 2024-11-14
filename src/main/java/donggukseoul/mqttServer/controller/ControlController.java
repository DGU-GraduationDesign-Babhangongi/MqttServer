package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.dto.ClassroomFanStatusDTO;
import donggukseoul.mqttServer.service.ControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/control")
@RequiredArgsConstructor
public class ControlController {

    private final ControlService controlService;

    @GetMapping("/classroom-fan-status")
    public ResponseEntity<List<ClassroomFanStatusDTO>> getClassroomFanStatus() {
        List<ClassroomFanStatusDTO> fanStatuses = controlService.getClassroomFanStatuses();
        return ResponseEntity.ok(fanStatuses);
    }
}
