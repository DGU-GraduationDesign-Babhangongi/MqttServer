package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.dto.CustomUserDetails;
import donggukseoul.mqttServer.entity.User;
import donggukseoul.mqttServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userData = userRepository.findByUsername(username);
        if (userData != null){
            return new CustomUserDetails(userData);
        }

        return null;
    }

}
