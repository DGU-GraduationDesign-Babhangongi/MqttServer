package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.dto.JoinDTO;
import donggukseoul.mqttServer.exception.CustomExceptions;
import donggukseoul.mqttServer.service.JoinService;
import donggukseoul.mqttServer.service.TokenBlacklistService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import donggukseoul.mqttServer.exception.CustomExceptions.*;


@Controller
@ResponseBody
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/api/join")
    public ResponseEntity<String> joinProcess(@RequestBody JoinDTO joinDTO) {
        try {
            joinService.joinProcess(joinDTO);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (InvalidVerificationCodeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (EmailNotAllowedException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("이메일 전송 중 오류가 발생했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }

    @PostMapping("/api/sendSecurityCode")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        try {
            joinService.sendVerificationCode(email);
            return ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다.");
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("이메일 전송 중 오류가 발생했습니다.");
        } catch (EmailNotAllowedException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
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
