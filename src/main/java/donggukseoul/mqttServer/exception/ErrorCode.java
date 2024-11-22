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

    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-001", "User already exists"),
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "USER-002", "Invalid user ID"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-003", "User not found"),

    ALLOWED_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "ALLOWED_EMAIL-001", "Allowed email already exists"),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "ALLOWED_EMAIL-002", "Email not found"),

    INVALID_CLASSROOM_ID(HttpStatus.BAD_REQUEST, "CLASSROOM-001", "Invalid classroom ID"),
    Invalid_CLASSROOM_NAME(HttpStatus.BAD_REQUEST, "CLASSROOM-002", "Invalid classroom name"),
    CLASSROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CLASSROOM-003", "Classroom not found for given building and name"),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN-001", "Invalid token"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN-002", "Token expired"),
    TOKEN_BLACKLISTED(HttpStatus.UNAUTHORIZED, "TOKEN-003", "Token blacklisted"),

    INVALID_MEMO_ID(HttpStatus.BAD_REQUEST, "MEMO-001", "Invalid memo ID"),

    INVALID_DEVICE_ID(HttpStatus.BAD_REQUEST, "DEVICE-001", "Invalid device ID"),

    INVALID_SENSOR_TYPE(HttpStatus.BAD_REQUEST, "SENSOR-001", "Invalid sensor type"),
    SENSOR_NOT_FOUND(HttpStatus.NOT_FOUND, "SENSOR-002", "Sensor not found"),;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}