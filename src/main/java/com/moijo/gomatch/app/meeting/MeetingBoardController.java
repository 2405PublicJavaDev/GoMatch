package com.moijo.gomatch.app.meeting;

import com.moijo.gomatch.common.BoardFileUtil;
import com.moijo.gomatch.domain.meeting.service.MeetingBoardService;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardFileVO;
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

    private String checkLogin(HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            throw new IllegalStateException("로그인이 필요한 서비스입니다.");
        }
        return memberId;
    }

    /**
     * 담당자: 김윤경
     * 관련 기능: [Show] 게시글 등록 페이지 보여주기
     * 설명: 게시글 등록을 위한 페이지 출력
     */
    @GetMapping("/board/register")
    public String showAddBoardPage(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        String memberNickName = (String) session.getAttribute("memberNickName");
        if (memberId == null) {
            return "common/oops";
        }else {
            model.addAttribute("loggedIn", true);
            model.addAttribute("memberNickName", memberNickName);
        }
        return "board/board-register";
    }

    /**
     * 담당자: 김윤경
     * 관련 기능: [Show] 게시글 리스트 페이지 보여주기
     * 설명: 필터 및 검색어를 기반으로 게시글 리스트 출력
     */
    @GetMapping("/board/list")
    public String showBoardListPage(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "filterType", defaultValue = "all") String filterType,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpSession session, Model model) {

        String memberId = (String) session.getAttribute("memberId");
        String memberNickName = (String) session.getAttribute("memberNickName");
        model.addAttribute("loggedIn", memberId != null);
        model.addAttribute("memberNickName", memberNickName);

        int pageSize = 10;
        int totalBoardCount = mBoardService.getBoardCount(filterType, searchType, keyword);
        int totalPages = (int) Math.ceil((double) totalBoardCount / pageSize);

        List<MeetingBoardVO> boardList = mBoardService.getBoardList(page, pageSize, filterType, searchType, keyword);
        // 각 게시글의 좋아요 수를 설정
        for (MeetingBoardVO board : boardList) {
            int likeCount = mBoardService.getLikeCount(board.getMeetingBoardNo());
            board.setLikeCount(likeCount);
        }
        model.addAttribute("boardList", boardList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("filterType", filterType);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);

        return "board/board-list";
    }

    /**
     * 담당자: 김윤경
     * 관련 기능: [Show - API] 모든 게시글 조회
     * 설명: 모든 게시글을 리스트 형태로 반환하는 API
     */
    @GetMapping("/boards")
    public ResponseEntity<List<MeetingBoardVO>> getAllBoards() {
        List<MeetingBoardVO> boardList = mBoardService.getAllBoards();
        return ResponseEntity.ok(boardList);
    }

    /**
     * 담당자: 김윤경
     * 관련 기능: [Show] 게시글 상세페이지 보여주기
     * 설명: 게시글 상세 정보와 좋아요, 댓글 정보를 포함하여 출력
     */
    @GetMapping("/board/detail/{meetingBoardNo}")
    public String showBoardDetail(@PathVariable("meetingBoardNo") long meetingBoardNo, HttpSession session, Model model) {
        // 조회수 증가
        mBoardService.increaseViewCount(meetingBoardNo);

        // 게시글 정보 가져오기
        MeetingBoardVO board = mBoardService.getBoardDetail(meetingBoardNo);
        String memberId = (String) session.getAttribute("memberId");
        model.addAttribute("loggedIn", memberId != null);

        // 로그인한 사용자의 닉네임 설정
        String memberNickName = (String) session.getAttribute("memberNickName");
        model.addAttribute("memberNickName", memberNickName);

        // 좋아요 상태 및 좋아요 수 가져오기
        boolean isLiked = memberId != null && mBoardService.checkLikeStatus(meetingBoardNo, memberId);
        int likeCount = mBoardService.getLikeCount(meetingBoardNo);

        // Markdown을 HTML로 변환하여 게시글 본문에 설정
        String htmlContent = mBoardService.markdownToHtml(board.getMeetingBoardContent());
        board.setMeetingBoardContent(htmlContent);

        // 댓글 및 파일 목록, 이전/다음 게시글 정보 가져오기
        List<MeetingBoardReplyVO> replies = mBoardService.getRepliesByBoardId(meetingBoardNo);
        Long previousPostId = mBoardService.getPreviousPostId(meetingBoardNo);
        Long nextPostId = mBoardService.getNextPostId(meetingBoardNo);
        List<MeetingBoardFileVO> meetingBoardFiles = mBoardService.getMeetingBoardFiles(meetingBoardNo);

        // 등록일 형식 포맷팅
        String formattedRegDate = board.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        // 모델에 값 설정
        model.addAttribute("board", board);
        model.addAttribute("formattedRegDate", formattedRegDate);
        model.addAttribute("replies", replies); // 댓글 목록에 닉네임 포함
        model.addAttribute("previousPostId", previousPostId);
        model.addAttribute("nextPostId", nextPostId);
        model.addAttribute("meetingBoardFile", meetingBoardFiles);
        model.addAttribute("isLiked", isLiked);
        model.addAttribute("likeCount", likeCount);

        return "board/board-detail";
    }

    /**
     * 담당자: 김윤경
     * 관련 기능: [Show] 게시글 수정 페이지 보여주기
     * 설명: 게시글 수정 폼 출력
     */
    @GetMapping("/board/modify/{meetingBoardNo}")
    public String showModifyBoardPage(@PathVariable("meetingBoardNo") long meetingBoardNo
            , HttpSession session, Model model ) {
        String memberId = (String) session.getAttribute("memberId");
        model.addAttribute("loggedIn", true);
        String memberNickName = (String) session.getAttribute("memberNickName");
        model.addAttribute("memberNickName", memberNickName);
        if(memberId == null) {
            return "common/oops";
        }
        MeetingBoardVO board = mBoardService.getBoardDetail(meetingBoardNo);
        if(!memberId.equals(board.getMemberId())){
            return "common/oops";
        }
        List<MeetingBoardFileVO> boardFiles = mBoardService.getMeetingBoardFiles(meetingBoardNo);
        model.addAttribute("board", board);
        model.addAttribute("boardFile", boardFiles);
        return "board/board-modify";
    }

    /**
     * 담당자: 김윤경
     * 관련 기능: [Register] 게시글 등록하기
     * 설명: 세션에서 memberId를 불러와 게시글을 등록하고, 파일 업로드 처리
     */
    @PostMapping("/board/register")
    public String addBoard(MeetingBoardVO mBoardVO,
                           @RequestParam(value = "groupImage", required = false) List<MultipartFile> groupImages,
                           HttpSession session) throws IOException {
        String memberId = checkLogin(session); // 로그인 체크
        mBoardVO.setMemberId(memberId);
        // 게시글 등록
        mBoardService.addBoard(mBoardVO);
        // 파일 업로드 처리
        if (!groupImages.isEmpty()) {
            boardFileUtil.uploadFiles(groupImages, Long.valueOf(mBoardVO.getMeetingBoardNo()), "board");
        }

        return "redirect:/board/list";
    }

    /**
     * 담당자: 김윤경
     * 관련 기능: [Register] 토스트 에디터 이미지 등록
     * 설명: 게시글 작성 시 에디터를 통해 이미지 파일을 업로드하고 URL 반환
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
     * 담당자: 김윤경
     * 관련 기능: [Modify] 게시글 수정 처리
     * 설명: 게시글 정보 및 첨부파일 수정
     */
    @PostMapping("/board/modify")
    public String modifyBoard(MeetingBoardVO boardVO,
                              @RequestParam(value = "newFiles", required = false) List<MultipartFile> newFiles,
                              @RequestParam(value = "fileDeleteIds", required = false) List<Long> fileDeleteIds,
                              HttpSession session) throws IOException {

        String memberId = checkLogin(session); // 로그인 체크
        MeetingBoardVO existingBoard = mBoardService.getBoardDetail(boardVO.getMeetingBoardNo());

        if (!memberId.equals(existingBoard.getMemberId())) {
            return "common/oops"; // 권한 없는 경우 처리
        }
        boardVO.setMemberId(memberId);
        mBoardService.modifyBoard(boardVO);
        // 파일 삭제 처리
        if (fileDeleteIds != null && !fileDeleteIds.isEmpty()) {
            mBoardService.deleteBoardFiles(fileDeleteIds);
        }
        // 새 파일 업로드 처리
        if (newFiles != null && !newFiles.isEmpty()) {
            boardFileUtil.uploadFiles(newFiles, boardVO.getMeetingBoardNo(), "board");
        }
        return "redirect:/board/detail/" + boardVO.getMeetingBoardNo();
    }

    /**
     * 담당자: 김윤경
     * 관련 기능: [Delete] 게시글 삭제
     * 설명: 게시글 삭제 처리
     */
    @PostMapping("/board/delete/{meetingBoardNo}")
    public String deleteBoard(@PathVariable("meetingBoardNo") long meetingBoardNo, HttpSession session) {

        String memberId = checkLogin(session); // 로그인 체크
        MeetingBoardVO board = mBoardService.getBoardDetail(meetingBoardNo);
        if (!memberId.equals(board.getMemberId())) {
            return "common/oops"; // 권한 없는 경우 처리
        }
        mBoardService.removeBoard(meetingBoardNo);
        return "redirect:/board/list";
    }

    // ■■■■■■■■■■■■■■■■■■■■■■■■■■ 게시글 좋아요/좋아요 취소 (MeetingBoardLike) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■ //

    /**
     * 담당자: 김윤경
     * 관련 기능: [Like] 게시글 좋아요 등록
     * 설명: 사용자가 게시글에 좋아요를 누르면 해당 게시글에 좋아요를 추가
     */
    @PostMapping("/board/like/{meetingBoardNo}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> likePost(@PathVariable("meetingBoardNo") long meetingBoardNo,
                                                        HttpSession session) {

        String memberId = checkLogin(session); // 로그인 체크
        Map<String, Object> response = new HashMap<>();

        // 이미 좋아요 상태인지 확인
        boolean alreadyLiked = mBoardService.checkLikeStatus(meetingBoardNo, memberId);
        if (alreadyLiked) {
            response.put("alreadyLiked", true);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        mBoardService.likePost(meetingBoardNo, memberId);
        int likeCount = mBoardService.getLikeCount(meetingBoardNo);
        log.info("게시글 {}의 현재 좋아요 수: {}", meetingBoardNo, likeCount);
        response.put("liked", true);
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }

    /**
     * 담당자: 김윤경
     * 관련 기능: [Unlike] 게시글 좋아요 취소
     * 설명: 사용자가 게시글에 누른 좋아요를 취소
     */
    @PostMapping("/board/unlike/{meetingBoardNo}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> unlikePost(@PathVariable("meetingBoardNo") long meetingBoardNo,
                                                          HttpSession session) {

        String memberId = checkLogin(session); // 로그인 체크
        mBoardService.unlikePost(meetingBoardNo, memberId);

        int likeCount = mBoardService.getLikeCount(meetingBoardNo);
        Map<String, Object> response = new HashMap<>();
        log.info("게시글 {}의 현재 좋아요 수: {}", meetingBoardNo, likeCount);
        response.put("liked", false);
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }


    // ■■■■■■■■■■■■■■■■■■■■■■■■■■ 게시글 댓글 등록 (MeetingBoardReply) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■ //
    /**
     * 담당자: 김윤경
     * 관련 기능: [Register] 댓글 등록
     * 설명: 게시글에 댓글을 등록
     */
    @PostMapping("/board/reply")
    public ResponseEntity<Map<String, Object>> addReply(Model model,
                                                        @RequestParam String meetingReplyContent,
                                                        @RequestParam long meetingBoardNo,
                                                        HttpSession session) {

        String memberId = checkLogin(session); // 로그인 체크
        boolean success = mBoardService.addReply(meetingBoardNo, memberId, meetingReplyContent);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);

        return ResponseEntity.ok(response);
    }
    /**
     * 담당자: 김윤경
     * 관련 기능: [Delete] 댓글 삭제
     * 설명: 게시글의 댓글을 삭제
     */
    @PostMapping("/board/reply/delete/{meetingReplyNo}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteReply(Model model,
                                                           @PathVariable long meetingReplyNo,
                                                           HttpSession session) {
        String memberId = checkLogin(session); // 로그인 체크
        boolean success = mBoardService.deleteReply(meetingReplyNo);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);

        return ResponseEntity.ok(response);
    }


}


