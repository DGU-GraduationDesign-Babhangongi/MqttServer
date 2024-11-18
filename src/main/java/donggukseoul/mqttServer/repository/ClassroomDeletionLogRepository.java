package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.ClassroomDeletionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomDeletionLogRepository extends JpaRepository<ClassroomDeletionLog, Long> {
}