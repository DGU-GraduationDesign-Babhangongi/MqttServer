//package donggukseoul.mqttServer.repository;
//
//import donggukseoul.mqttServer.entity.SensorDataIaqIndex;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface SensorDataIaqIndexRepository extends SensorDataRepository<SensorDataIaqIndex> {
//
//    Page<SensorDataIaqIndex> findBySensorId(String sensorId, Pageable pageable);
//
//    SensorDataIaqIndex findFirstBySensorIdOrderByTimestampDesc(String sensorId);
//
//}
