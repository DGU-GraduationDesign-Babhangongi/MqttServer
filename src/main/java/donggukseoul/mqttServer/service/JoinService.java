package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.dto.JoinDTO;
import donggukseoul.mqttServer.entity.User;
import donggukseoul.mqttServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
}
