package donggukseoul.mqttServer.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinDTO {

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private String areaOfResponsibility;

    private String securityCode;

    private boolean alarmStatus;

}
