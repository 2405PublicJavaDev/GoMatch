package com.moijo.gomatch.app.chatting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moijo.gomatch.domain.chatting.service.ChatService;
import com.moijo.gomatch.domain.chatting.vo.ChatMessage;
import com.moijo.gomatch.domain.chatting.vo.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    /**
     * 담당자: 홍예은
     * 관련 기능: [Handle] 클라이언트로부터 전달받은 메시지를 처리
     * 설명:
     * - 사용자가 입장 시: 해당 사용자를 세션에 추가하고 입장 메시지 전달
     * - 사용자가 퇴장 시: 세션에서 제거하고 퇴장 메시지 전달
     * - 일반 메시지: 전달받은 메시지를 그대로 방의 모든 사용자에게 전송
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
        Set<WebSocketSession> sessions=room.getSessions();   // 해당 방에 있는 사용자들의 WebSocket 세션
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            // 사용자가 방에 입장하면 세션에 추가하고 입장 메시지 전달
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");  //TALK일 경우 msg가 있을 거고, ENTER일 경우 메세지 없으니까 message set
            sendToEachSocket(sessions,new TextMessage(objectMapper.writeValueAsString(chatMessage)) );
        }else if (chatMessage.getType().equals(ChatMessage.MessageType.QUIT)) {
            sessions.remove(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장했습니다..");
            sendToEachSocket(sessions,new TextMessage(objectMapper.writeValueAsString(chatMessage)) );
        }else {
            // 입장/퇴장이 아닐 경우, 전달받은 메시지를 그대로 모든 사용자에게 전송
            sendToEachSocket(sessions,message );
        }
    }

    /**
     * 담당자: 홍예은
     * 관련 기능: [Send] 해당 채팅방에 있는 모든 사용자에게 메시지 전송
     * 설명: 전달받은 메시지를 각 WebSocket 세션에 병렬로 전송
     */
    private  void sendToEachSocket(Set<WebSocketSession> sessions, TextMessage message){
        sessions.parallelStream().forEach( roomSession -> {
            try {
                roomSession.sendMessage(message); // 메시지 전송
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}