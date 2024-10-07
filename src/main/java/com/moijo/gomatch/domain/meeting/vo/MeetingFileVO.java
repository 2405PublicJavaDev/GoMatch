package com.moijo.gomatch.domain.meeting.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class MeetingFileVO {
    private long meetingFileNo;
    private String fileName;
    private String fileRename;
    private String filePath;
    private Timestamp regDate;
    private Timestamp updateDate;
    private String webPath;
    private int fileOrder;
    private long meetingNo;
}
