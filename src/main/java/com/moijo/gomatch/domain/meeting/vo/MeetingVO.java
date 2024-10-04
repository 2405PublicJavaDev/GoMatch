package com.moijo.gomatch.domain.meeting.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class MeetingVO {
    private long meetingNo;
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
