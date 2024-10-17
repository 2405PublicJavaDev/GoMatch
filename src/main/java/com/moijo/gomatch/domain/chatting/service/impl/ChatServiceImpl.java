package com.moijo.gomatch.domain.chatting.service.impl;

import com.moijo.gomatch.domain.chatting.mapper.ChatMapper;
import com.moijo.gomatch.domain.chatting.service.ChatService;
import com.moijo.gomatch.domain.chatting.vo.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMapper chatMapper;

    @Transactional
    @Override
    public Chat saveMessage(Chat chat) {
        chat.setSendDate(Timestamp.valueOf(LocalDateTime.now()));  // 보낸 시간 설정
        chatMapper.insertChat(chat);  // 채팅 저장
        return chat;
    }

    @Override
    public List<Chat> getChatsByRoomId(String roomId) {
        return chatMapper.getChatsByRoomId(roomId);  // 방 ID로 채팅 목록 조회
    }
}