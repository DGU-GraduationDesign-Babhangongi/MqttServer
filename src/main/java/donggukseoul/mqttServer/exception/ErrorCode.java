package donggukseoul.mqttServer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EMAIL_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "EMAIL-001", "Email not allowed"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "EMAIL-002", "Email already exists"),
    INVALID_VERIFICATION_CODE(HttpStatus.UNAUTHORIZED, "VERIFICATION-001", "Invalid verification code"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-001", "User already exists");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}