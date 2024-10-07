package com.moijo.gomatch.domain.meeting.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class MeetingAttendVO {
    private long meetingAttendNo;    // 소모임 참석 번호
    private int meetingNo;          // 소모임 번호
    private String memberId;        // 참석한 사용자 ID
    private String memberNickname; // 참석자 닉네임
    private String meetingAttendYn; // 참석 여부 (Y/N)
    private Timestamp regDate;      // 참석 등록일
    private Timestamp updateDate;   // 참석 수정일
}