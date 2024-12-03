package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorDataRepository<T extends SensorData> extends JpaRepository<T, Long> {
}