package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.Classroom;
import donggukseoul.mqttServer.entity.Memo;
import donggukseoul.mqttServer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findByUser(User user);

    List<Memo> findByClassroom(Classroom classroom);

}
