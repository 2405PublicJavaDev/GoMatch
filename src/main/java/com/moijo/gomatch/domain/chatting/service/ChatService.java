package com.moijo.gomatch.domain.chatting.service;

import com.moijo.gomatch.domain.chatting.vo.Chat;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatService {

    @Transactional
    Chat saveMessage(Chat chat);

    List<Chat> getChatsByRoomId(String roomId);
}
