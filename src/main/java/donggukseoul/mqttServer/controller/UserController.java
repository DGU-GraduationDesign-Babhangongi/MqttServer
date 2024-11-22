package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.service.JoinService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final JoinService joinService;
    @GetMapping("nickname")
    public String getNickname(HttpServletRequest request) throws ServletException, IOException {
        return joinService.getNickname(request);
    }
}
