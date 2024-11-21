package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.dto.JoinDTO;
import donggukseoul.mqttServer.service.JoinService;
import donggukseoul.mqttServer.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/api/join")
    public ResponseEntity<String> joinProcess(JoinDTO joinDTO) {

        joinService.joinProcess(joinDTO);

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/api/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.split(" ")[1];
            tokenBlacklistService.addTokenToBlacklist(token);
        }
        return ResponseEntity.ok().build();
    }

}
