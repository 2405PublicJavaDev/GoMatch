package com.moijo.gomatch.common;

import com.moijo.gomatch.domain.meeting.mapper.MeetingBoardMapper;
import com.moijo.gomatch.domain.meeting.vo.MeetingBoardFileVO;
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
public class BoardFileUtil {

    private final MeetingBoardMapper meetingBoardMapper;

    public int uploadFiles(List<MultipartFile> files, Long meetingBoardId, String moduleName) throws IOException {
        int result = 0;
        long order = 1;

        String folderPath = BoardFileConfig.getModuleUploadPath(moduleName);  // 모듈별 경로 설정
        File folder = new File(folderPath);
        if (!folder.exists()) {
            // 경로가 없으면 폴더 생성
            boolean isCreated = folder.mkdirs();
            if (!isCreated) {
                throw new IOException("파일을 저장할 경로를 생성할 수 없습니다: " + folder.getAbsolutePath());
            }
        }
        for (MultipartFile file : files) {
            if(file.getOriginalFilename() != null && !file.getOriginalFilename().isBlank()) {
                String fileName = file.getOriginalFilename();
                String extension = fileName.substring(fileName.lastIndexOf("."));
                String fileRename = UUID.randomUUID().toString() + extension;
                String filePath = folderPath + fileRename;

                File targetFile = new File(filePath);
                file.transferTo(targetFile);

                if (targetFile.exists()) {
                    String webPath = "/uploads/" + moduleName + "/" + fileRename;
                    result += insertFileData(meetingBoardId, fileName, fileRename, filePath, webPath, order);
                    order++;
                } else {
                    throw new IOException("파일이 저장되지 않았습니다: " + targetFile.getAbsolutePath());
                }
            }
        }
        return result;
    }

    private int insertFileData(Long meetingBoardId, String fileName, String fileRename, String filePath, String webPath, Long order) {
        MeetingBoardFileVO meetingBoardFileVO = new MeetingBoardFileVO();
        meetingBoardFileVO.setMeetingBoardNo(meetingBoardId.intValue());
        meetingBoardFileVO.setFileName(fileName);
        meetingBoardFileVO.setFileRename(fileRename);
        meetingBoardFileVO.setFilePath(filePath);
        meetingBoardFileVO.setWebPath(webPath);
        meetingBoardFileVO.setMeetingBoardFileNo((long) order.intValue());

        return meetingBoardMapper.insertBoardFile(meetingBoardFileVO);
    }
}
