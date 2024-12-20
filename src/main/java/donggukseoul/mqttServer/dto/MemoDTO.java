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

    // 추가된 필드: 작성자 여부
    private boolean isAuthor;
}
