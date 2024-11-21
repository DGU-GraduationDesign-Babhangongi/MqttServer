package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.dto.AllowedEmailDTO;
import donggukseoul.mqttServer.entity.AllowedEmail;
import donggukseoul.mqttServer.service.AllowedEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class AllowedEmailController {
    private final AllowedEmailService allowedEmailService;

    @PostMapping
    public ResponseEntity<String> addAllowedEmail(@RequestParam String email) {
        allowedEmailService.addAllowedEmail(email);
        return ResponseEntity.ok("허용된 이메일이 추가되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> removeAllowedEmail(@RequestParam String email) {
        allowedEmailService.removeAllowedEmail(email);
        return ResponseEntity.ok("허용된 이메일이 삭제되었습니다.");
    }

    @GetMapping
    public ResponseEntity<List<AllowedEmailDTO>> getAllAllowedEmails() {
        List<AllowedEmail> allowedEmails = allowedEmailService.getAllAllowedEmails();
        List<AllowedEmailDTO> allowedEmailDTOs = allowedEmails.stream()
                .map(email -> {
                    AllowedEmailDTO dto = new AllowedEmailDTO();
                    dto.setId(email.getId());
                    dto.setEmail(email.getEmail());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(allowedEmailDTOs);
    }
}