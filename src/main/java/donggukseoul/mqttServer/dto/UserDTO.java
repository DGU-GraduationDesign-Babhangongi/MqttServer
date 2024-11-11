package donggukseoul.mqttServer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String nickname;
    private String email;
}
