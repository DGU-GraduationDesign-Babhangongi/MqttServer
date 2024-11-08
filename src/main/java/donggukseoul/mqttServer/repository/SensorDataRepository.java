//package donggukseoul.mqttServer.repository;
//
//import donggukseoul.mqttServer.dto.SensorDataDTO;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.repository.query.Param;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//@Repository
//public interface SensorDataRepository extends JpaRepository<SensorDataDTO, Long> {
//
//    @Query(value = "SELECT sd.sensor_id AS sensorId, sd.timestamp AS timestamp, sd.value AS value, 'Temperature' AS sensorType " +
//            "FROM sensor_data_temperature sd WHERE sd.sensor_id = :sensorId AND sd.timestamp BETWEEN :startDate AND :endDate " +
//            "UNION ALL " +
//            "SELECT sd.sensor_id AS sensorId, sd.timestamp AS timestamp, sd.value AS value, 'TVOC' AS sensorType " +
//            "FROM sensor_data_tvoc sd WHERE sd.sensor_id = :sensorId AND sd.timestamp BETWEEN :startDate AND :endDate " +
//            "UNION ALL " +
//            "SELECT sd.sensor_id AS sensorId, sd.timestamp AS timestamp, sd.value AS value, 'AmbientNoise' AS sensorType " +
//            "FROM sensor_data_ambient_noise sd WHERE sd.sensor_id = :sensorId AND sd.timestamp BETWEEN :startDate AND :endDate " +
//            "UNION ALL " +
//            "SELECT sd.sensor_id AS sensorId, sd.timestamp AS timestamp, sd.value AS value, 'IAQIndex' AS sensorType " +
//            "FROM sensor_data_iaq_index sd WHERE sd.sensor_id = :sensorId AND sd.timestamp BETWEEN :startDate AND :endDate " +
//            "UNION ALL " +
//            "SELECT sd.sensor_id AS sensorId, sd.timestamp AS timestamp, sd.value AS value, 'AQMScores' AS sensorType " +
//            "FROM sensor_data_aqm_scores sd WHERE sd.sensor_id = :sensorId AND sd.timestamp BETWEEN :startDate AND :endDate " +
//            "UNION ALL " +
//            "SELECT sd.sensor_id AS sensorId, sd.timestamp AS timestamp, sd.value AS value, 'Humidity' AS sensorType " +
//            "FROM sensor_data_humidity sd WHERE sd.sensor_id = :sensorId AND sd.timestamp BETWEEN :startDate AND :endDate " +
//            "UNION ALL " +
//            "SELECT sd.sensor_id AS sensorId, sd.timestamp AS timestamp, CASE WHEN sd.value THEN 1.0 ELSE 0.0 END AS value, 'UsbPowered' AS sensorType " +
//            "FROM sensor_data_usb_powered sd WHERE sd.sensor_id = :sensorId AND sd.timestamp BETWEEN :startDate AND :endDate " +
//            "UNION ALL " +
//            "SELECT sd.sensor_id AS sensorId, sd.timestamp AS timestamp, CASE WHEN sd.value THEN 1.0 ELSE 0.0 END AS value, 'ButtonPressed' AS sensorType " +
//            "FROM sensor_data_button_pressed sd WHERE sd.sensor_id = :sensorId AND sd.timestamp BETWEEN :startDate AND :endDate " +
//            "UNION ALL " +
//            "SELECT sd.sensor_id AS sensorId, sd.timestamp AS timestamp, CASE WHEN sd.value THEN 1.0 ELSE 0.0 END AS value, 'WaterDetection' AS sensorType " +
//            "FROM sensor_data_water_detection sd WHERE sd.sensor_id = :sensorId AND sd.timestamp BETWEEN :startDate AND :endDate " +
//            "UNION ALL " +
//            "SELECT sd.sensor_id AS sensorId, sd.timestamp AS timestamp, sd.value AS value, 'PM2_5MassConcentration' AS sensorType " +
//            "FROM sensor_data_pm2_5_mass_concentration sd WHERE sd.sensor_id = :sensorId AND sd.timestamp BETWEEN :startDate AND :endDate " +
//            "ORDER BY timestamp",
//            nativeQuery = true)
//    List<SensorDataDTO> findAllSensorDataBetweenDates(@Param("sensorId") String sensorId,
//                                                      @Param("startDate") LocalDateTime startDate,
//                                                      @Param("endDate") LocalDateTime endDate,
//                                                      Pageable pageable);
//}
