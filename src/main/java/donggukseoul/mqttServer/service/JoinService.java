package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.dto.JoinDTO;
import donggukseoul.mqttServer.entity.User;


import donggukseoul.mqttServer.exception.CustomException;
import donggukseoul.mqttServer.exception.ErrorCode;
import donggukseoul.mqttServer.jwt.JWTUtil;
import donggukseoul.mqttServer.repository.UserRepository;
import donggukseoul.mqttServer.util.VerificationUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JWTUtil jwtUtil;

    private final VerificationService verificationService;

    private final EmailService emailService;

    private final AllowedEmailService allowedEmailService;

    private final TokenBlacklistService tokenBlacklistService;


    public void joinProcess(JoinDTO joinDTO) {

        if (!verificationService.verifyCode(joinDTO.getEmail(), joinDTO.getSecurityCode())) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        if (!allowedEmailService.isAllowedEmail(joinDTO.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_NOT_ALLOWED);
        }

        Boolean isExist = userRepository.existsByUsername(joinDTO.getUsername());

        if (isExist) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(joinDTO.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User data = new User();

        data.setUsername(joinDTO.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        data.setNickname(joinDTO.getNickname());
        data.setEmail(joinDTO.getEmail());
        data.setAreaOfResponsibility(joinDTO.getAreaOfResponsibility());
        data.setAlarmStatus(joinDTO.isAlarmStatus());
        data.setRole("ROLE_ADMIN");

        userRepository.save(data);

        verificationService.invalidateCode(joinDTO.getEmail());

    }

    public void sendVerificationCode(String email) throws MessagingException {

        if (!allowedEmailService.isAllowedEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_NOT_ALLOWED);
        }

        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String code = VerificationUtil.generateVerificationCode();
        verificationService.saveVerificationCode(email, code);
        emailService.sendVerificationEmail(email, code);
    }

    public String getNickname(HttpServletRequest request) throws ServletException, IOException {
        return getUserFromRequest(request).getNickname();
    }

    public User getUserFromRequest(HttpServletRequest request) throws ServletException, IOException {
        String token = jwtUtil.validateToken(request,null,null, tokenBlacklistService);
        if (token == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);


        return userRepository.findByUsername(username);
    }

}
