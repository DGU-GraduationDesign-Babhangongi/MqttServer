package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.entity.AllowedEmail;
import donggukseoul.mqttServer.exception.CustomException;
import donggukseoul.mqttServer.exception.ErrorCode;
import donggukseoul.mqttServer.repository.AllowedEmailRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AllowedEmailService {
    private static final Logger logger = LoggerFactory.getLogger(AllowedEmailService.class);
    private final AllowedEmailRepository allowedEmailRepository;

    public void addAllowedEmail(String email) {
        if (!isEmailExists(email)) {
            AllowedEmail allowedEmail = new AllowedEmail();
            allowedEmail.setEmail(email);
            allowedEmailRepository.save(allowedEmail);
        } else {
            logger.warn("Email already exists: {}", email);
            throw new CustomException(ErrorCode.ALLOWED_EMAIL_ALREADY_EXISTS);
        }
    }

    @Transactional
    public void removeAllowedEmail(String email) {
        if (!isEmailExists(email)) {
            logger.warn("Email not found: {}", email);
            throw new CustomException(ErrorCode.EMAIL_NOT_FOUND);
        }
        allowedEmailRepository.deleteByEmail(email);
    }

    public List<AllowedEmail> getAllAllowedEmails() {
        return allowedEmailRepository.findAll();
    }

    public boolean isAllowedEmail(String email) {
        return isEmailExists(email);
    }

    private boolean isEmailExists(String email) {
        return allowedEmailRepository.existsByEmail(email);
    }
}