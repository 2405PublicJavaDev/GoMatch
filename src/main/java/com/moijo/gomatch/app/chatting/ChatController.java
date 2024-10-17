package com.moijo.gomatch.app.chatting;

import com.moijo.gomatch.domain.chatting.service.ChatService;
import com.moijo.gomatch.domain.chatting.vo.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller  // HTML을 반환할 때는 @RestController 대신 @Controller 사용
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * STOMP 메시지 처리: 클라이언트가 "/app/chat"로 메시지 전송 시 처리
     * @param chat 클라이언트로부터 받은 채팅 메시지
     * @return 전송된 메시지를 다시 브로드캐스트
     */
    @MessageMapping("/chat")
    @SendTo("/topic/public")  // 메시지를 /topic/public으로 구독자들에게 전송
    public Chat sendMessage(Chat chat) {
        return chatService.saveMessage(chat);
    }

    /**
     * 특정 방의 모든 채팅 기록 조회
     * @param roomId 방 ID
     * @return 채팅 목록 반환
     */
    @GetMapping("/chats/{roomId}")
    public List<Chat> getChatsByRoomId(@PathVariable String roomId) {
        return chatService.getChatsByRoomId(roomId);  // 방의 모든 채팅 조회
    }

    /**
     * 채팅 페이지 렌더링
     * @return chat.html 페이지 반환
     */
    @GetMapping("/chat")
    public String chatPage(Model model) {
        model.addAttribute("roomId", 1);  // 예시로 방 ID를 전달
        return "chat/chat";  // src/main/resources/templates/chat/chat.html 반환
    }
}
