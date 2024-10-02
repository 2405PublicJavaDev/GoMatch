package com.moijo.gomatch.domain.meeting.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class MeetingVO {
    private int meetingNo;
    private int gameNo;
    private String meetingTitle;
    private String meetingTeamName;
    private int meetingMaxPeople;
    private String meetingTime;
    private String meetingPlace;
    private String meetingContent;
    private String memberId;
    private Timestamp regDate;
    private Timestamp updateDate;
}
