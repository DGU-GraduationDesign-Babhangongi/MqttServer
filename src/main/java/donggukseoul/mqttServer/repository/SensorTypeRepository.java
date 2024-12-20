package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.SensorTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SensorTypeRepository extends JpaRepository<SensorTypeEntity, Long> {
    boolean existsByTypeName(String typeName);
    Optional<SensorTypeEntity> findByTypeName(String typeName);
}
