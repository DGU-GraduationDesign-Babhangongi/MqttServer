//package donggukseoul.mqttServer.repository;
//
//import donggukseoul.mqttServer.entity.SensorDataUsbPowered;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface SensorDataUsbPoweredRepository extends SensorDataRepository<SensorDataUsbPowered> {
//    Page<SensorDataUsbPowered> findBySensorId(String sensorId, Pageable pageable);
//    SensorDataUsbPowered findFirstBySensorIdOrderByTimestampDesc(String sensorId);
//
//}
