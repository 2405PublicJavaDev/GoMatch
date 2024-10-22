package com.moijo.gomatch.app.chatting;

import com.moijo.gomatch.domain.chatting.service.ChatService;

import com.moijo.gomatch.domain.chatting.vo.ChatRoom;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    /**
     * 담당자: 홍예은
     * 관련 기능: [Login Check] 로그인 여부 확인
     * 설명: 현재 세션에 저장된 멤버 아이디를 확인하여 로그인 여부를 JSON으로 반환
     */
    @GetMapping("/chat/checkLogin")
    @ResponseBody
    public Map<String, Boolean> checkLogin(HttpSession session) {
        Map<String, Boolean> response = new HashMap<>();
        String memberId = (String) session.getAttribute("memberId");
        response.put("loggedIn", memberId != null);
        return response;
    }

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("memberNickName", member.getMemberNickName());
        } else {
            model.addAttribute("loggedIn", false);
        }
    }


    /**
     * 담당자: 홍예은
     * 관련 기능: [Show] 소모임 별 채팅 리스트 출력
     * 설명: 소모임 별 채팅 리스트 출력하기
     */
    @RequestMapping("/chat/chatList/{meetingNo}")
    public String showChatListPage(@PathVariable long meetingNo, Model model, HttpSession session) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member == null) {
            return "common/oops";
        }
        List<ChatRoom> roomList = chatService.findAllRoom(meetingNo);
        roomList.sort(Comparator.comparing(ChatRoom::getCreatedDate).reversed());
        model.addAttribute("roomList",roomList);
        model.addAttribute("meetingNo", meetingNo);
        return "chat/chatList";
    }

    /**
     * 담당자: 홍예은
     * 관련 기능: [Register] 채팅방 생성
     * 설명: 채팅방 이름을 입력하여 채팅방 생성하기
     */
    @PostMapping("/chat/createRoom/{meetingNo}")  //방 만들기
    public String createRoom(Model model, @PathVariable long meetingNo, @RequestParam String roomName, HttpSession session) {
        String memberNickName = (String) session.getAttribute("memberNickName");
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member == null) {
            // 세션이 없을 경우 처리
            return "common/oops";
        }
        String profileUrl = member.getProfileImageUrl() != null ? member.getProfileImageUrl() : "/img/기본프로필.png";
        System.out.println(profileUrl);
        ChatRoom room = chatService.createRoom(roomName, meetingNo);
        model.addAttribute("room",room);
        model.addAttribute("memberNickName",memberNickName);
        model.addAttribute("member",member);
        model.addAttribute("profileUrl",profileUrl);
        model.addAttribute("meetingNo", meetingNo);
        return "chat/chatRoom";  //만든사람이 채팅방을 처음으로 들어가게 됨
    }

    /**
     * 담당자: 홍예은
     * 관련 기능: [Show] 해당 채팅방 입장
     * 설명: 해당 채팅방 페이지로 이동하기
     */
    @GetMapping("/chat/chatRoom/{meetingNo}/{roomId}")
    public String chatRoom(Model model, @PathVariable long meetingNo, @PathVariable String roomId, HttpSession session) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member == null) {
            return "common/oops";
        }
        ChatRoom room = chatService.findRoomById(roomId);
        String profileUrl = member.getProfileImageUrl() != null ? member.getProfileImageUrl() : "/img/기본프로필.png";
        model.addAttribute("member",member);
        model.addAttribute("profileUrl",profileUrl);
        model.addAttribute("room",room);   // 현재 방의 정보를 보냄
        model.addAttribute("meetingNo", meetingNo);
        return "chat/chatRoom";
    }
}