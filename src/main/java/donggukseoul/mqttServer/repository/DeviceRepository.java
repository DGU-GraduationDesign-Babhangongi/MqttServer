package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.Classroom;
import donggukseoul.mqttServer.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByClassroom(Classroom classroom);
}