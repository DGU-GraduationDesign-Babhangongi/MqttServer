package donggukseoul.mqttServer.repository;

import donggukseoul.mqttServer.entity.FanStatusLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FanStatusLogRepository extends JpaRepository<FanStatusLog, Long> {

    // 특정 강의실에 대해 가장 최근 4개의 팬 상태 로그 조회
    @Query("SELECT f FROM FanStatusLog f WHERE f.classroom = :classroom ORDER BY f.timestamp DESC")
    List<FanStatusLog> findTop4ByClassroomOrderByTimestampDesc(@Param("classroom") int classroom, Pageable pageable);
}
