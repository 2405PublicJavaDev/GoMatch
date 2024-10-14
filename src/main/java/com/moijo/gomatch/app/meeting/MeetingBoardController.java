package com.moijo.gomatch.app.meeting;

import com.moijo.gomatch.common.BoardFileUtil;
import com.moijo.gomatch.domain.meeting.service.MeetingBoardService;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardFileVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardLikeVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardReplyVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
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
    /**
     * 담당자 : 김윤경
     * 관련 기능 : [페이지 폼] 게시글 등록하기
     * 설명 : 토스트 에디터 이미지 등록
     */
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
    /**
     * 담당자 : 김윤경
     * 관련 기능 : [페이지 폼] 게시글 등록하기
     * 설명 : 소모임 개설 페이지 보여주기
     */
    @GetMapping("/board/list")
    public String showBoardListPage(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpSession session, Model model) {

        String memberId = (String) session.getAttribute("memberId");
        String memberNickName = (String) session.getAttribute("memberNickName");

        int pageSize = 10;
        int totalBoardCount = mBoardService.getBoardCount(searchType, keyword);
        int totalPages = (int) Math.ceil((double) totalBoardCount / pageSize);

        List<MeetingBoardVO> boardList = mBoardService.getBoardList(page, pageSize, searchType, keyword);

        model.addAttribute("boardList", boardList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);

        return "board/board-list";
    }

    // 모든 게시글을 반환하는 API
    @GetMapping("/boards")
    public ResponseEntity<List<MeetingBoardVO>> getAllBoards() {
        List<MeetingBoardVO> boardList = mBoardService.getAllBoards();
        return ResponseEntity.ok(boardList);
    }

    // 게시글 상세 조회 메서드 추가
    @GetMapping("/board/detail/{meetingBoardNo}")
    public String showBoardDetail(@PathVariable("meetingBoardNo") long meetingBoardNo, HttpSession session, Model model) {
        // 게시글 정보 가져오기
        MeetingBoardVO board = mBoardService.getBoardDetail(meetingBoardNo);
        String memberId = (String) session.getAttribute("memberId");
        model.addAttribute("loggedIn", true);
        String memberNickName = (String) session.getAttribute("memberNickName");
        model.addAttribute("memberNickName", memberNickName);
        log.info("세션에서 가져온 memberId: " + memberId);
        // Markdown을 HTML로 변환
        String htmlContent = mBoardService.markdownToHtml(board.getMeetingBoardContent());
        board.setMeetingBoardContent(htmlContent);

        // 댓글, 이전/다음 게시글 정보 가져오기
        List<MeetingBoardReplyVO> replies = mBoardService.getRepliesByBoardId(meetingBoardNo);
        Long previousPostId = mBoardService.getPreviousPostId(meetingBoardNo);
        Long nextPostId = mBoardService.getNextPostId(meetingBoardNo);
        List<MeetingBoardFileVO> meetingBoardFiles = mBoardService.getMeetingBoardFiles(meetingBoardNo);

        // 등록일 형식 포맷팅
        String formattedRegDate = board.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        // 모델에 값 설정
        model.addAttribute("formattedRegDate", formattedRegDate);
        model.addAttribute("board", board);
        model.addAttribute("replies", replies);
        model.addAttribute("previousPostId", previousPostId);
        model.addAttribute("nextPostId", nextPostId);
        model.addAttribute("meetingBoardFile", meetingBoardFiles);
        model.addAttribute("loggedIn", session.getAttribute("memberId") != null);

        return "board/board-detail";
    }

    // ■■■■■■■■■■■■■■■■■■■■■■■■■■ 게시글 좋아요/좋아요 취소 (MeetingBoardLike) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■ //


    @PostMapping("/board/like/{meetingBoardNo}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> likePost(@PathVariable("meetingBoardNo") long meetingBoardNo, HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        Map<String, Object> response = new HashMap<>();

        if (memberId == null) {
            response.put("loginRequired", true);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        mBoardService.likePost(meetingBoardNo, memberId);
        response.put("liked", true);
        response.put("likeCount", mBoardService.getLikeCount(meetingBoardNo));

        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/board/unlike/{meetingBoardNo}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> unlikePost(@PathVariable("meetingBoardNo") long meetingBoardNo, HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        Map<String, Object> response = new HashMap<>();

        if (memberId == null) {
            response.put("loginRequired", true);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        mBoardService.unlikePost(meetingBoardNo, memberId);
        response.put("liked", false);
        response.put("likeCount", mBoardService.getLikeCount(meetingBoardNo));

        return ResponseEntity.ok(response);
    }


    // ■■■■■■■■■■■■■■■■■■■■■■■■■■ 게시글 댓글 등록 (MeetingBoardReply) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■ //

    // 댓글 등록 메서드
    @PostMapping("/board/reply")
    public ResponseEntity<Map<String, Object>> addReply(@RequestParam String content, @RequestParam long boardId, HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean success = mBoardService.addReply(boardId, memberId, content);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);

        return ResponseEntity.ok(response);
    }
}

