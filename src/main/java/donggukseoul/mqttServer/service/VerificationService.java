package donggukseoul.mqttServer.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class VerificationService {
    private final Map<String, String> verificationCodes = new HashMap<>();
    private final Map<String, Long> expirationTimes = new HashMap<>();
    private static final long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(10);

    public void saveVerificationCode(String email, String code) {
        verificationCodes.put(email, code);
        expirationTimes.put(email, System.currentTimeMillis() + EXPIRATION_TIME);
    }

    public boolean verifyCode(String email, String code) {
        if (!verificationCodes.containsKey(email)) {
            return false;
        }
        if (System.currentTimeMillis() > expirationTimes.get(email)) {
            verificationCodes.remove(email);
            expirationTimes.remove(email);
            return false;
        }
        return verificationCodes.get(email).equals(code);
    }

    public void invalidateCode(String email) {
        verificationCodes.remove(email);
        expirationTimes.remove(email);
    }
}