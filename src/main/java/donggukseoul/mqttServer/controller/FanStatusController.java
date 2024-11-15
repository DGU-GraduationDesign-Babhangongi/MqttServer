package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.entity.FanStatusLog;
import donggukseoul.mqttServer.repository.FanStatusLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/classroom")
public class FanStatusController {

    private final FanStatusLogRepository fanStatusLogRepository;

    @GetMapping("/{classroom}/fan-logs")
    public ResponseEntity<List<FanStatusLog>> getRecentFanLogs(@PathVariable int classroom) {
        // Get the most recent 4 fan status logs for the specified classroom
        Pageable pageable = PageRequest.of(0, 4); // Page size set to 4
        List<FanStatusLog> recentFanLogs = fanStatusLogRepository.findTop4ByClassroomOrderByTimestampDesc(classroom, pageable);

        if (recentFanLogs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        return ResponseEntity.ok(recentFanLogs);
    }
}

