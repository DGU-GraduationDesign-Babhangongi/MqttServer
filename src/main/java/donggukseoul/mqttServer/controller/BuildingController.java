package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.dto.BuildingCreateDTO;
import donggukseoul.mqttServer.dto.BuildingDTO;
import donggukseoul.mqttServer.dto.BuildingDetailDTO;
import donggukseoul.mqttServer.dto.ClassroomDTO;
import donggukseoul.mqttServer.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingService buildingService;

    @GetMapping()
    public ResponseEntity<List<BuildingDetailDTO>> getAllBuildingNames() {
        List<BuildingDetailDTO> buildings = buildingService.getAllBuildings();
        return ResponseEntity.ok(buildings);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<BuildingDetailDTO> createBuilding(
            @RequestParam("name") String name,
            @RequestParam("maxFloor") int maxFloor,
            @RequestPart("buildingImage") MultipartFile buildingImage,
            @RequestPart("floorPlans") List<MultipartFile> floorPlans) throws IOException {

        BuildingCreateDTO buildingCreateDto = new BuildingCreateDTO();
        buildingCreateDto.setName(name);
        buildingCreateDto.setMaxFloor(maxFloor);

        BuildingDetailDTO buildingDetail = buildingService.createBuilding(buildingCreateDto, buildingImage, floorPlans);
        return ResponseEntity.ok(buildingDetail);
    }

    // 건물명과 층수를 입력받아 해당 층에 등록된 강의실 리스트 반환(센서 id, 타입, 위치)
    @GetMapping("/{buildingName}/floors/{floor}/classrooms")
    public ResponseEntity<List<ClassroomDTO>> getClassroomsByBuildingAndFloor(
            @PathVariable String buildingName,
            @PathVariable int floor) {
        List<ClassroomDTO> classrooms = buildingService.getClassroomsByBuildingAndFloor(buildingName, floor);
        return ResponseEntity.ok(classrooms);
    }

}