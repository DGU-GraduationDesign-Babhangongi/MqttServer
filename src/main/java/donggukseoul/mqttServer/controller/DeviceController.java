package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.dto.DeviceDTO;
import donggukseoul.mqttServer.service.DeviceService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;


    @GetMapping("/classroom")
    public ResponseEntity<List<DeviceDTO>> getDevicesByClassroom(@RequestParam String building, @RequestParam String name) {
        List<DeviceDTO> devices = deviceService.getDevicesByClassroom(building, name);
        return ResponseEntity.ok(devices);
    }

    @PostMapping("/toggle/deviceId")
    public ResponseEntity<DeviceDTO> toggleDeviceStatus(@RequestParam Long deviceId) throws MessagingException {
        DeviceDTO device = deviceService.toggleDeviceStatus(deviceId);
        return ResponseEntity.ok(device);
    }

    @PostMapping("/add")
    public ResponseEntity<DeviceDTO> addDeviceToClassroom(@RequestParam String building, @RequestParam String name, @RequestParam String type) {
        DeviceDTO device = deviceService.addDeviceToClassroom(building, name, type);
        return ResponseEntity.ok(device);
    }

    @DeleteMapping("/remove/deviceId")
    public ResponseEntity<Void> removeDevice(@RequestParam Long deviceId) {
        deviceService.removeDevice(deviceId);
        return ResponseEntity.noContent().build();
    }
}