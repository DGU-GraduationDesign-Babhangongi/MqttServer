package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    @Query("SELECT sensorId FROM Classroom WHERE building = :building AND name = :name")
    String findSensorIdByBuildingAndName(String building, String name);

}
