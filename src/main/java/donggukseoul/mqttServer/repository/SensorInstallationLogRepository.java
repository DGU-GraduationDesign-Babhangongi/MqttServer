package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.SensorInstallationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorInstallationLogRepository extends JpaRepository<SensorInstallationLog, Long> {
}
