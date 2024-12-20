// BuildingService.java
package donggukseoul.mqttServer.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import donggukseoul.mqttServer.dto.BuildingCreateDTO;
import donggukseoul.mqttServer.dto.BuildingDTO;
import donggukseoul.mqttServer.dto.BuildingDetailDTO;
import donggukseoul.mqttServer.dto.FloorPlanDTO;
import donggukseoul.mqttServer.entity.Building;
import donggukseoul.mqttServer.entity.FloorPlan;
import donggukseoul.mqttServer.repository.BuildingRepository;
import donggukseoul.mqttServer.repository.ClassroomRepository;
import donggukseoul.mqttServer.repository.FloorPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final FloorPlanRepository floorPlanRepository;
    private final ClassroomRepository classroomRepository;

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyFileName;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public BuildingDTO createBuilding(BuildingCreateDTO buildingCreateDto, List<MultipartFile> floorPlans) throws IOException {
        Building building = Building.builder()
                .name(buildingCreateDto.getName())
                .maxFloor(buildingCreateDto.getMaxFloor())
                .build();
        buildingRepository.save(building);

        List<FloorPlan> savedFloorPlans = new ArrayList<>();
        for (int i = 0; i < floorPlans.size(); i++) {
            MultipartFile file = floorPlans.get(i);
            String fileName = "floorplans/" + building.getName() + "/floor_" + (i + 1) + ".jpg";
            String imageUrl = uploadFileToGCS(file, fileName);

            FloorPlan floorPlan = FloorPlan.builder()
                    .floor(i + 1)
                    .imageUrl(imageUrl)
                    .building(building)
                    .build();
            savedFloorPlans.add(floorPlan);
        }
        floorPlanRepository.saveAll(savedFloorPlans);

        building.setFloorPlans(savedFloorPlans);
        return convertToDTO(building);
    }

    public List<BuildingDetailDTO> getAllBuildings() {
        return buildingRepository.findAll().stream()
                .map(this::convertToDetailDTOWithSensorCount)
                .collect(Collectors.toList());
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

    private BuildingDTO convertToDTO(Building building) {
        BuildingDTO buildingDTO = new BuildingDTO();
        buildingDTO.setId(building.getId());
        buildingDTO.setName(building.getName());
        buildingDTO.setMaxFloor(building.getMaxFloor());
        buildingDTO.setFloorPlans(building.getFloorPlans().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
        return buildingDTO;
    }

    private FloorPlanDTO convertToDTO(FloorPlan floorPlan) {
        FloorPlanDTO floorPlanDTO = new FloorPlanDTO();
        floorPlanDTO.setId(floorPlan.getId());
        floorPlanDTO.setFloor(floorPlan.getFloor());
        floorPlanDTO.setImageUrl(floorPlan.getImageUrl());
        return floorPlanDTO;
    }

    private BuildingDetailDTO convertToDetailDTOWithSensorCount(Building building) {
        BuildingDetailDTO buildingDetailDTO = new BuildingDetailDTO();
        buildingDetailDTO.setId(building.getId());
        buildingDetailDTO.setName(building.getName());
        buildingDetailDTO.setMaxFloor(building.getMaxFloor());

        // 층별 이미지 URL 리스트 생성
        List<String> floorPlanImages = building.getFloorPlans().stream()
                .map(floorPlan -> floorPlan.getImageUrl())
                .collect(Collectors.toList());
        buildingDetailDTO.setFloorPlanImages(floorPlanImages);

        // 해당 빌딩에 등록된 센서 개수 계산
        int sensorCount = classroomRepository.countByBuilding(building.getName());
        buildingDetailDTO.setSensorCount(sensorCount);

        return buildingDetailDTO;
    }

}