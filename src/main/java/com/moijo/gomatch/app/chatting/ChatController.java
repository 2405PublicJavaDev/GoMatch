package com.moijo.gomatch.app.chatting;

import com.moijo.gomatch.domain.chatting.service.ChatService;

import com.moijo.gomatch.domain.chatting.vo.ChatRoom;
import com.moijo.gomatch.domain.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @RequestMapping("/chat/chatList")
    public String chatList(Model model){
        List<ChatRoom> roomList = chatService.findAllRoom();
        model.addAttribute("roomList",roomList);
        return "chat/chatList";
    }

    @PostMapping("/chat/createRoom")  //방 만들기
    public String createRoom(Model model, @RequestParam String roomName, HttpSession session) {
        String memberNickName = (String) session.getAttribute("memberNickName");
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member == null) {
            // 세션이 없을 경우 처리
            return "common/oops";
        }
        String profileUrl = member.getProfileImageUrl() != null ? member.getProfileImageUrl() : "/img/기본프로필.png";
        System.out.println(profileUrl);
        ChatRoom room = chatService.createRoom(roomName);
        model.addAttribute("room",room);
        model.addAttribute("memberNickName",memberNickName);
        model.addAttribute("member",member);
        model.addAttribute("profileUrl",profileUrl);
        return "chat/chatRoom";  //만든사람이 채팅방을 처음으로 들어가게 됨
    }

    @GetMapping("/chat/chatRoom")
    public String chatRoom(Model model, @RequestParam String roomId, HttpSession session) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member == null) {
            return "common/oops";
        }
        ChatRoom room = chatService.findRoomById(roomId);
        String profileUrl = member.getProfileImageUrl() != null ? member.getProfileImageUrl() : "/img/기본프로필.png";
        model.addAttribute("member",member);
        model.addAttribute("profileUrl",profileUrl);
        model.addAttribute("room",room);   // 현재 방의 정보를 보냄
        return "chat/chatRoom";
    }
}