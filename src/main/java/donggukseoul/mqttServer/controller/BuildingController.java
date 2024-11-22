package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.dto.BuildingCreateDTO;
import donggukseoul.mqttServer.dto.BuildingDTO;
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
    public ResponseEntity<List<String>> getAllBuildingNames() {
        List<String> buildingNames = buildingService.getAllBuildingNames();
        return ResponseEntity.ok(buildingNames);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<BuildingDTO> createBuilding(
            @RequestParam("name") String name,
            @RequestParam("maxFloor") int maxFloor,
            @RequestPart("floorPlans") List<MultipartFile> floorPlans) throws IOException {

        BuildingCreateDTO buildingCreateDto = new BuildingCreateDTO();
        buildingCreateDto.setName(name);
        buildingCreateDto.setMaxFloor(maxFloor);

        BuildingDTO building = buildingService.createBuilding(buildingCreateDto, floorPlans);
        return ResponseEntity.ok(building);
    }
}