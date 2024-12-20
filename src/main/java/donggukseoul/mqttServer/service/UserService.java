package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.dto.AlarmStatusDTO;
import donggukseoul.mqttServer.entity.User;
import donggukseoul.mqttServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public AlarmStatusDTO toggleAlarmStatus(User user) {
        boolean currentStatus = user.isAlarmStatus();
        user.setAlarmStatus(!currentStatus);
        userRepository.save(user);
        return new AlarmStatusDTO(user.isAlarmStatus());
    }

    public AlarmStatusDTO getAlarmStatus(User user) {
        return new AlarmStatusDTO(user.isAlarmStatus());
    }
}
