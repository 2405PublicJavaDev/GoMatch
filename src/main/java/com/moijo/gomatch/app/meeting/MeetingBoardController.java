package com.moijo.gomatch.app.meeting;

import com.moijo.gomatch.common.BoardFileConfig;
import com.moijo.gomatch.common.BoardFileUtil;
import com.moijo.gomatch.domain.meeting.service.MeetingBoardService;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardFileVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MeetingBoardController {

    private final MeetingBoardService mBoardService;
    private final BoardFileUtil boardFileUtil;

    /**
     * 담당자 : 김윤경
     * 관련 기능 : [페이지 폼] 게시글 등록하기
     * 설명 : 소모임 개설 페이지 보여주기
     */
    @GetMapping("/board/register")
    public String showAddBoardPage(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        String memberNickName = (String) session.getAttribute("memberNickName");
        if (memberId == null) {
            return "로그인이 필요합니다.";
        }else {
            model.addAttribute("loggedIn", true);
            model.addAttribute("memberNickName", memberNickName);
        }
        return "board/board-register";
    }

    /**
     * 담당자 : 김윤경
     * 관련 기능 : [페이지 폼] 게시글 등록하기
     * 설명 : 소모임 개설 페이지 보여주기
     */
    @PostMapping("/board/register")
    public String addBoard(MeetingBoardVO mBoardVO
            , @RequestParam(value = "groupImage", required = false) List<MultipartFile> groupImages
            , HttpSession session) throws IOException {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return "로그인이 필요합니다.";
        }
        mBoardVO.setMemberId(memberId);
        // 게시글 등록
        mBoardService.addBoard(mBoardVO);
        // 파일 정보 저장
        if(!groupImages.isEmpty()) {
            boardFileUtil.uploadFiles(groupImages, Long.valueOf(mBoardVO.getMeetingBoardNo()), "board");
        }
        return "redirect:/board/list";
        }

    @PostMapping("/upload/image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String uploadDir = "C:/gomatch/board/toast/";
            File uploadFile = new File(uploadDir, fileName);

            if (!uploadFile.getParentFile().exists()) {
                uploadFile.getParentFile().mkdirs();
            }

            file.transferTo(uploadFile);

            // /images/ 경로를 통해 이미지에 접근할 수 있도록 설정
            String imageUrl = "/images/" + fileName;
            Map<String, String> response = new HashMap<>();
            response.put("url", imageUrl);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
