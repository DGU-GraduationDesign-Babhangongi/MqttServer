package donggukseoul.mqttServer.service;

import donggukseoul.mqttServer.dto.MemoDTO;
import donggukseoul.mqttServer.entity.Classroom;
import donggukseoul.mqttServer.entity.Memo;
import donggukseoul.mqttServer.entity.User;
import donggukseoul.mqttServer.exception.CustomException;
import donggukseoul.mqttServer.exception.ErrorCode;
import donggukseoul.mqttServer.repository.ClassroomRepository;
import donggukseoul.mqttServer.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final ClassroomRepository classroomRepository;

    public MemoDTO addMemo(String building, String name, String content, User user) {
        Classroom classroom = classroomRepository.findByBuildingAndName(building, name)
                .orElseThrow(() -> new CustomException(ErrorCode.Invalid_CLASSROOM_NAME));

        Memo memo = Memo.builder()
                .user(user)
                .classroom(classroom)
                .content(content)
                .createdAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();

        Memo savedMemo = memoRepository.save(memo);
        return convertToMemoDTO(savedMemo, user);
    }

    public void deleteMemo(Long memoId, User user) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMO_ID));

        if (!memo.getUser().equals(user)) {
            throw new CustomException(ErrorCode.INVALID_USER_ID);
        }

        memoRepository.delete(memo);
    }

    public List<MemoDTO> getUserMemos(User user) {
        return memoRepository.findByUser(user)
                .stream()
                .map(memo -> convertToMemoDTO(memo, user))
                .collect(Collectors.toList());
    }

    public List<MemoDTO> getClassroomMemos(String building, String name, User user) {
        Optional<Classroom> classroom = classroomRepository.findByBuildingAndName(building, name);

        if (classroom.isEmpty()) {
            throw new CustomException(ErrorCode.Invalid_CLASSROOM_NAME);
        }

        return memoRepository.findByClassroom(classroom.get())
                .stream()
                .map(memo -> convertToMemoDTO(memo, user))
                .collect(Collectors.toList());
    }

    private MemoDTO convertToMemoDTO(Memo memo, User currentUser) {
        return MemoDTO.builder()
                .id(memo.getId())
                .content(memo.getContent())
                .createdAt(memo.getCreatedAt())
                .username(memo.getUser().getUsername())
                .nickname(memo.getUser().getNickname())
                .building(memo.getClassroom().getBuilding())
                .floor(memo.getClassroom().getFloor())
                .name(memo.getClassroom().getName())
                .isAuthor(memo.getUser().equals(currentUser))
                .build();
    }
}
