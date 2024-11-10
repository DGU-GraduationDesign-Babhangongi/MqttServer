package donggukseoul.mqttServer.controller;


import donggukseoul.mqttServer.enums.SensorType;
import donggukseoul.mqttServer.enums.SortBy;
import donggukseoul.mqttServer.enums.SortOrder;
import donggukseoul.mqttServer.service.JoinService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final JoinService joinService;
    @GetMapping("nickname")
    public String getNickname(HttpServletRequest request) {
        return joinService.getNickname(request);
    }
}
