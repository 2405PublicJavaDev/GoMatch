package com.moijo.gomatch.common;

import com.moijo.gomatch.domain.meeting.mapper.MeetingMapper;
import com.moijo.gomatch.domain.meeting.vo.MeetingFileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class MeetingFileUtil {

    private final MeetingMapper meetingMapper;

    public int uploadFiles(List<MultipartFile> files, Long meetingId, String moduleName) throws IOException {
        int result = 0;
        long order = 1;

        // 모듈별 저장 경로 설정
        String folderPath = MeetingFileConfig.getModuleUploadPath(moduleName);  // 모듈별 경로 설정
        File folder = new File(folderPath);
        if (!folder.exists()) {
            // 경로가 없으면 폴더 생성
            boolean isCreated = folder.mkdirs();
            if (!isCreated) {
                throw new IOException("파일을 저장할 경로를 생성할 수 없습니다: " + folder.getAbsolutePath());
            }
        }

        // 파일 저장 처리
        for (MultipartFile file : files) {
            if (file.getOriginalFilename() != null && !file.getOriginalFilename().isBlank()) {
                String fileName = file.getOriginalFilename();
                String extension = fileName.substring(fileName.lastIndexOf("."));
                String fileRename = UUID.randomUUID().toString() + extension;
                String filePath = folderPath + fileRename;

                // 파일 저장
                file.transferTo(new File(filePath));

                // WEB_PATH 생성
                String webPath = "/uploads/" + moduleName + "/" + fileRename;

                // DB에 파일 정보 저장
                result += insertFileData(meetingId, fileName, fileRename, filePath, webPath, order);
                order++;
            }
        }
        return result;
    }

    private int insertFileData(Long meetingId, String fileName, String fileRename, String filePath, String webPath, Long order) {
        MeetingFileVO meetingFileVO = new MeetingFileVO();
        meetingFileVO.setMeetingNo(meetingId.intValue());
        meetingFileVO.setFileName(fileName);
        meetingFileVO.setFileRename(fileRename);
        meetingFileVO.setFilePath(filePath);
        meetingFileVO.setWebPath(webPath);  // WEB_PATH 설정
        meetingFileVO.setFileOrder(order.intValue());

        // DB에 파일 정보 저장
        return meetingMapper.insertMeetingFile(meetingFileVO);
    }
}
