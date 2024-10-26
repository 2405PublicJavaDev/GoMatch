package com.moijo.gomatch.domain.chatting.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moijo.gomatch.domain.chatting.service.ChatService;
import com.moijo.gomatch.domain.chatting.vo.ChatRoom;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    @Override
    public void init() {
        chatRooms = new LinkedHashMap<>();
    }

    // ■■■■■■■■■■■■■■■■■■■ 채팅방 정보 (CHATTING) ■■■■■■■■■■■■■■■■■■■■ //
    // 소모임 별 채팅방 리스트 출력
    @Override
    public List<ChatRoom> findAllRoom(long meetingNo) {
        return chatRooms.values().stream()
                .filter(room -> room.getMeetingNo() == meetingNo)  // meetingNo가 일치하는 채팅방만 필터링
                .collect(Collectors.toList());
    }

    // 채팅방 입장
    @Override
    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    // 채팅방 생성
    @Override
    public ChatRoom createRoom(String name, long meetingNo) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .meetingNo(meetingNo)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }
}