package donggukseoul.mqttServer.controller;

import donggukseoul.mqttServer.dto.MemoDTO;
import donggukseoul.mqttServer.entity.User;
import donggukseoul.mqttServer.service.JoinService;
import donggukseoul.mqttServer.service.MemoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;
    private final JoinService joinService;

    @PostMapping("/add")
    public ResponseEntity<MemoDTO> addMemo(@RequestParam String building, @RequestParam String name, @RequestParam String content, HttpServletRequest request) throws ServletException, IOException {
        User user = joinService.getUserFromRequest(request);
        MemoDTO memo = memoService.addMemo(building, name, content, user);
        return ResponseEntity.ok(memo);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMemo(@RequestParam Long memoId, HttpServletRequest request) throws ServletException, IOException {
        User user = joinService.getUserFromRequest(request);
        memoService.deleteMemo(memoId, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<MemoDTO>> getUserMemos(HttpServletRequest request) throws ServletException, IOException {
        User user = joinService.getUserFromRequest(request);
        List<MemoDTO> memos = memoService.getUserMemos(user);
        return ResponseEntity.ok(memos);
    }

    @GetMapping("/classroom")
    public ResponseEntity<List<MemoDTO>> getClassroomMemos(@RequestParam String building, @RequestParam String name, HttpServletRequest request) throws ServletException, IOException {
        User user = joinService.getUserFromRequest(request);
        List<MemoDTO> memos = memoService.getClassroomMemos(building, name, user);
        return ResponseEntity.ok(memos);
    }
}
