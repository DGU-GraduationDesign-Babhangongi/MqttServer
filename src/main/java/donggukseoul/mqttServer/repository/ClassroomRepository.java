package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    @Query("SELECT sensorId FROM Classroom WHERE building = :building AND name = :name")
    String findSensorIdByBuildingAndName(String building, String name);

    Optional<Classroom> findByBuildingAndName(String building, String name);

    List<Classroom> findByBuilding(String building);

    int countByBuilding(String building);

    List<Classroom> findByBuildingAndFloor(String building, int floor);

    boolean existsBySensorId(String sensorId);

    Optional<Classroom> findBySensorId(String sensorId);

}
