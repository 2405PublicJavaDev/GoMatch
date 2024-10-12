package com.moijo.gomatch.domain.meeting.mapper;

import com.moijo.gomatch.domain.meeting.vo.MeetingBoardFileVO;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MeetingBoardMapper {

    // 게시글 등록 메서드
    void insertBoard(MeetingBoardVO meetingBoardVO);
    // 파일 저장 메서드
    int insertBoardFile(MeetingBoardFileVO meetingBoardFileVO);
}
