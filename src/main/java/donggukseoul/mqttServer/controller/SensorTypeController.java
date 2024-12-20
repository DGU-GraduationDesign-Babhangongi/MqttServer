package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.entity.SensorTypeEntity;
import donggukseoul.mqttServer.repository.SensorTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensorTypes")
@RequiredArgsConstructor
public class SensorTypeController {

    private final SensorTypeRepository sensorTypeRepository;

    @GetMapping
    public List<SensorTypeEntity> getAllSensorTypes() {
        return sensorTypeRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<SensorTypeEntity> addSensorType(@RequestBody SensorTypeEntity sensorType) {
        if (sensorTypeRepository.existsByTypeName(sensorType.getTypeName())) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(sensorTypeRepository.save(sensorType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SensorTypeEntity> updateSensorType(@PathVariable Long id, @RequestBody SensorTypeEntity updatedSensorType) {
        return sensorTypeRepository.findById(id)
                .map(sensorType -> {
                    sensorType.setTypeName(updatedSensorType.getTypeName());
                    return ResponseEntity.ok(sensorTypeRepository.save(sensorType));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensorType(@PathVariable Long id) {
        if (sensorTypeRepository.existsById(id)) {
            sensorTypeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
