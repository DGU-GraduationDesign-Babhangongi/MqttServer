package donggukseoul.mqttServer.service;

//import donggukseoul.mqttServer.dto.ClassroomDTO;
import donggukseoul.mqttServer.dto.MemoDTO;
//import donggukseoul.mqttServer.dto.UserDTO;
import donggukseoul.mqttServer.entity.Classroom;
import donggukseoul.mqttServer.entity.Memo;
import donggukseoul.mqttServer.entity.User;
import donggukseoul.mqttServer.repository.ClassroomRepository;
import donggukseoul.mqttServer.repository.MemoRepository;
import donggukseoul.mqttServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;

    public MemoDTO addMemo(String building, String name, String content, User user) {


        Classroom classroom = classroomRepository.findByBuildingAndName(building, name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid classroom ID"));

        Memo memo = Memo.builder()
                .user(user)
                .classroom(classroom)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        Memo savedMemo = memoRepository.save(memo);
        return convertToMemoDTO(savedMemo);
    }

    public void deleteMemo(Long memoId, User user) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid memo ID"));

        if (!memo.getUser().equals(user)) {
            throw new SecurityException("You can only delete your own memos");
        }

        memoRepository.delete(memo);
    }

    public List<MemoDTO> getUserMemos(User user) {
        return memoRepository.findByUser(user)
                .stream()
                .map(this::convertToMemoDTO)
                .collect(Collectors.toList());
    }

    public List<MemoDTO> getClassroomMemos(String building, String name) {
        Optional<Classroom> classroom = classroomRepository.findByBuildingAndName(building, name);

        if (classroom.isEmpty()) {
            throw new IllegalArgumentException("Invalid classroom information");
        }
        return memoRepository.findByClassroom(classroom.get())
                .stream()
                .map(this::convertToMemoDTO)
                .collect(Collectors.toList());
    }

    private MemoDTO convertToMemoDTO(Memo memo) {
        return MemoDTO.builder()
                .id(memo.getId())
                .content(memo.getContent())
                .createdAt(memo.getCreatedAt())
                .username(memo.getUser().getUsername())
                .nickname(memo.getUser().getNickname())
                .building(memo.getClassroom().getBuilding())
                .floor(memo.getClassroom().getFloor())
                .name(memo.getClassroom().getName())
                .build();
    }

//    private UserDTO convertToUserDTO(User user) {
//        return UserDTO.builder()
//                .id(user.getId())
//                .username(user.getUsername())
//                .nickname(user.getNickname())
//                .email(user.getEmail())
//                .build();
//    }
//
//    private ClassroomDTO convertToClassroomDTO(Classroom classroom) {
//        return ClassroomDTO.builder()
//                .id(classroom.getId())
//                .name(classroom.getName())
//                .floor(classroom.getFloor())
//                .building(classroom.getBuilding())
//                .sensorId(classroom.getSensorId())
//                .sensorType(classroom.getSensorType())
//                .build();
//    }
}
