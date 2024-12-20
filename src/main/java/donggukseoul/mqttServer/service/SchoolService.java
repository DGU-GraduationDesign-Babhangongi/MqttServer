package donggukseoul.mqttServer.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import donggukseoul.mqttServer.dto.SchoolCreateDTO;
import donggukseoul.mqttServer.dto.SchoolDTO;
import donggukseoul.mqttServer.entity.School;
import donggukseoul.mqttServer.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyFileName;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public List<SchoolDTO> getAllSchools() {
        return schoolRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SchoolDTO createSchool(SchoolCreateDTO schoolCreateDTO) throws IOException {
        String logoUrl = uploadFileToGCS(schoolCreateDTO.getLogo(), "schools/logos/" + schoolCreateDTO.getName() + ".jpg");

        School school = School.builder()
                .name(schoolCreateDTO.getName())
                .englishName(schoolCreateDTO.getEnglishName())
                .logoUrl(logoUrl)
                .address(schoolCreateDTO.getAddress())
                .adminEmail(schoolCreateDTO.getAdminEmail())
                .themeColor(schoolCreateDTO.getThemeColor())
                .build();

        schoolRepository.save(school);
        return convertToDTO(school);
    }

    private String uploadFileToGCS(MultipartFile multipartFile, String fileName) throws IOException {
        InputStream keyFile = ResourceUtils.getURL(keyFileName).openStream();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();

        String ext = multipartFile.getContentType();
        String imgUrl = "https://storage.googleapis.com/" + bucketName + "/" + fileName;

        if (!multipartFile.isEmpty()) {
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                    .setContentType(ext)
                    .build();
            storage.create(blobInfo, multipartFile.getInputStream());
        } else {
            imgUrl = null;
        }

        return imgUrl;
    }

    private SchoolDTO convertToDTO(School school) {
        SchoolDTO dto = new SchoolDTO();
        dto.setId(school.getId());
        dto.setName(school.getName());
        dto.setLogoUrl(school.getLogoUrl());
        dto.setThemeColor(school.getThemeColor());
        return dto;
    }
}
