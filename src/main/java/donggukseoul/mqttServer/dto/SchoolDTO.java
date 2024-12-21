package donggukseoul.mqttServer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolDTO {
    private Long id;
    private String name;
    private String englishName;
    private String logoUrl;
    private String address;
    private String themeColor;
}
