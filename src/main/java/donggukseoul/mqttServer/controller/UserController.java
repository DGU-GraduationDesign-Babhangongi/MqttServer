package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.dto.AlarmStatusDTO;
import donggukseoul.mqttServer.entity.User;
import donggukseoul.mqttServer.service.JoinService;
import donggukseoul.mqttServer.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final JoinService joinService;

    private final UserService userService;

    @GetMapping("nickname")
    public String getNickname(HttpServletRequest request) throws ServletException, IOException {
        return joinService.getNickname(request);
    }

    // isAlarmStatus 토글 API
    @PostMapping("/alarm/toggle")
    public ResponseEntity<AlarmStatusDTO> toggleAlarmStatus(HttpServletRequest request) throws ServletException, IOException {
        User user = joinService.getUserFromRequest(request);
        AlarmStatusDTO alarmStatus = userService.toggleAlarmStatus(user);
        return ResponseEntity.ok(alarmStatus);
    }

    // 현재 isAlarmStatus 값 반환 API
    @GetMapping("/alarm/status")
    public ResponseEntity<AlarmStatusDTO> getAlarmStatus(HttpServletRequest request) throws ServletException, IOException {
        User user = joinService.getUserFromRequest(request);
        AlarmStatusDTO alarmStatus = userService.getAlarmStatus(user);
        return ResponseEntity.ok(alarmStatus);
    }

}
