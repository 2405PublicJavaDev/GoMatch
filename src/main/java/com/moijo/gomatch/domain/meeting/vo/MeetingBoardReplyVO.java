package com.moijo.gomatch.domain.meeting.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class MeetingBoardReplyVO {
    private int meetingReplyNo;
    private String meetingReplyContent;
    private int meetingBoardNo;
    private String memberId;
    private Timestamp regDate;
    private Timestamp updateDate;
}
