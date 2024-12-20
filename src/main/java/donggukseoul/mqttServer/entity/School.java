package donggukseoul.mqttServer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;           // 학교명
    private String englishName;    // 학교 영어 이름
    private String logoUrl;        // 학교 로고 이미지 URL
    private String address;        // 학교 주소
    private String adminEmail;     // 최초 관리자 이메일
    // RGB 색상
    private int red;
    private int green;
    private int blue;
}
