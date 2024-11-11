package donggukseoul.mqttServer.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MemoDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;

    private String username;
    private String nickname;

    private String building;
    private Integer floor;
    private String name;

//    private UserDTO user;       // 작성자 정보 DTO
//    private ClassroomDTO classroom;  // 강의실 정보 DTO
}
