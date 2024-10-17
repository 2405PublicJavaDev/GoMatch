package com.moijo.gomatch.domain.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
@Getter
@Setter
@ToString
public class AdminMeetingVO {
        private long meetingNo;
        private int gameNo;
        private String meetingTitle;
        private String meetingTeamName;
        private int meetingMaxPeople;
        private String meetingTime;
        private String meetingPlace;
        private String meetingContent;
        private String memberId;
        private String meetingDate;
        private Timestamp regDate;
        private Timestamp updateDate;
}
