//package donggukseoul.mqttServer.repository;
//
//import donggukseoul.mqttServer.entity.SensorDataAqmScores;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface SensorDataAqmScoresRepository extends SensorDataRepository<SensorDataAqmScores> {
//
//    Page<SensorDataAqmScores> findBySensorId(String sensorId, Pageable pageable);
//
//    SensorDataAqmScores findFirstBySensorIdOrderByTimestampDesc(String sensorId);
//
//}