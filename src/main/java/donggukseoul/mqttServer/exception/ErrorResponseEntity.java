package donggukseoul.mqttServer.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Builder
@Data
public class ErrorResponseEntity {
    private final int status;
    private final String code;
    private final String message;


   public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode errorCode) {
       return ResponseEntity.status(errorCode.getHttpStatus())
               .body(ErrorResponseEntity.builder()
                       .status(errorCode.getHttpStatus().value())
                       .code(errorCode.getCode())
                       .message(errorCode.getMessage())
                       .build());
   }

}