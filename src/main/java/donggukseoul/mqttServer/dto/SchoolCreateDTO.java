package donggukseoul.mqttServer.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SchoolCreateDTO {
    private String name;
    private String englishName;
    private MultipartFile logo;
    private String address;
    private String adminEmail;

    // RGB 색상
    private int red;
    private int green;
    private int blue;
}
