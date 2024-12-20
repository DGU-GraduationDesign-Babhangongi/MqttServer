package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.dto.SchoolCreateDTO;
import donggukseoul.mqttServer.dto.SchoolDTO;
import donggukseoul.mqttServer.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    // 학교 리스트 반환 API
    @GetMapping
    public ResponseEntity<List<SchoolDTO>> getAllSchools() {
        List<SchoolDTO> schools = schoolService.getAllSchools();
        return ResponseEntity.ok(schools);
    }

    // 학교 추가 API
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<SchoolDTO> createSchool(
            @RequestParam("name") String name,
            @RequestParam("englishName") String englishName,
            @RequestParam("logo") MultipartFile logo,
            @RequestParam("address") String address,
            @RequestParam("adminEmail") String adminEmail,
            @RequestParam("themeColor") String themeColor) throws IOException {

        SchoolCreateDTO schoolCreateDTO = new SchoolCreateDTO();
        schoolCreateDTO.setName(name);
        schoolCreateDTO.setEnglishName(englishName);
        schoolCreateDTO.setLogo(logo);
        schoolCreateDTO.setAddress(address);
        schoolCreateDTO.setAdminEmail(adminEmail);
        schoolCreateDTO.setThemeColor(themeColor);

        SchoolDTO schoolDTO = schoolService.createSchool(schoolCreateDTO);
        return ResponseEntity.ok(schoolDTO);
    }
}
