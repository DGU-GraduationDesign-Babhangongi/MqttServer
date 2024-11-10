package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.dto.JoinDTO;
import donggukseoul.mqttServer.entity.User;
import donggukseoul.mqttServer.jwt.JWTUtil;
import donggukseoul.mqttServer.repository.UserRepository;
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


    public void joinProcess(JoinDTO joinDTO) {

//        String username = joinDTO.getUsername();
//        String password = joinDTO.getPassword();

        Boolean isExist = userRepository.existsByUsername(joinDTO.getUsername());

        if (isExist){

            return;
        }

        User data = new User();

        data.setUsername(joinDTO.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        data.setNickname(joinDTO.getNickname());
        data.setEmail(joinDTO.getEmail());
//        data.setPhoneNumber(joinDTO.getPhoneNumber());
        data.setAreaOfResponsibility(joinDTO.getAreaOfResponsibility());
//        data.setSecurityCode(joinDTO.getSecurityCode());
        data.setAlarmStatus(joinDTO.isAlarmStatus());
        data.setRole("ROLE_ADMIN");

        userRepository.save(data);

    }

    public String getNickname(HttpServletRequest request) {
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


        String nickname = userRepository.findByUsername(username).getNickname();
        return nickname;
    }

}
