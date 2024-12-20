package donggukseoul.mqttServer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolDTO {
    private Long id;
    private String name;
    private String logoUrl;

    // RGB 색상
    private int red;
    private int green;
    private int blue;
}
