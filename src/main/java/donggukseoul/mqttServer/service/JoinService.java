package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.dto.JoinDTO;
import donggukseoul.mqttServer.entity.User;
import donggukseoul.mqttServer.jwt.JWTUtil;
import donggukseoul.mqttServer.repository.AllowedEmailRepository;
import donggukseoul.mqttServer.repository.UserRepository;
import donggukseoul.mqttServer.util.VerificationUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JWTUtil jwtUtil;

    private final VerificationService verificationService;

    private final EmailService emailService;

    private final AllowedEmailService allowedEmailService;


    public void joinProcess(JoinDTO joinDTO) throws MessagingException {

        if (!verificationService.verifyCode(joinDTO.getEmail(), joinDTO.getSecurityCode())) {
            throw new IllegalArgumentException("인증 코드가 유효하지 않습니다.");
        }

        if (!allowedEmailService.isAllowedEmail(joinDTO.getEmail())) {
            throw new IllegalArgumentException("허용되지 않은 이메일입니다.");
        }

        Boolean isExist = userRepository.existsByUsername(joinDTO.getUsername());

        if (isExist){

            return;
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
        String code = VerificationUtil.generateVerificationCode();
        verificationService.saveVerificationCode(email, code);
        emailService.sendVerificationEmail(email, code);
    }

    public String getNickname(HttpServletRequest request) {
        return getUserFromRequest(request).getNickname();
    }

    public User getUserFromRequest(HttpServletRequest request) {
        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("token null");
//            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return null;
        }

        System.out.println("authorization now");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");
//            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return null;
        }

        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);


        return userRepository.findByUsername(username);
    }

}
