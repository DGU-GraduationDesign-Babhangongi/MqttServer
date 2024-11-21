package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.entity.AllowedEmail;
import donggukseoul.mqttServer.repository.AllowedEmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AllowedEmailService {
    private final AllowedEmailRepository allowedEmailRepository;

    public void addAllowedEmail(String email) {
        if (!allowedEmailRepository.existsByEmail(email)) {
            AllowedEmail allowedEmail = new AllowedEmail();
            allowedEmail.setEmail(email);
            allowedEmailRepository.save(allowedEmail);
        }
    }

    @Transactional
    public void removeAllowedEmail(String email) {
        allowedEmailRepository.deleteByEmail(email);
    }

    public List<AllowedEmail> getAllAllowedEmails() {
        return allowedEmailRepository.findAll();
    }

    public boolean isAllowedEmail(String email) {
        return allowedEmailRepository.existsByEmail(email);
    }
}