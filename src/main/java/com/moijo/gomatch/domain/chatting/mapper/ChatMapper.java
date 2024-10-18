package com.moijo.gomatch.domain.chatting.mapper;

import com.moijo.gomatch.domain.chatting.vo.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {

    void insertChat(ChatMessage chat);  // XML에 정의된 쿼리를 실행

    List<ChatMessage> getChatsByRoomId(@Param("roomId") String room);  // 방 ID로 채팅 목록 조회
}
