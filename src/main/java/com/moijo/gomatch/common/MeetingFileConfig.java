package com.moijo.gomatch.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Getter
public class MeetingFileConfig implements WebMvcConfigurer {

    public static String baseFolderPath;  // static 선언

    @Value("${GOMATCH_UPLOAD_BASE_PATH}")
    public void setBaseFolderPath(String baseFolderPath) {
        MeetingFileConfig.baseFolderPath = baseFolderPath;
    }

    // 모듈별 저장 경로 반환
    public static String getModuleUploadPath(String moduleName) {
        return baseFolderPath + "/" + moduleName + "/";
    }

    // 리소스 핸들러 등록
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 웹 경로와 실제 파일 경로 매핑
        String webPath = "/uploads/**";  // 웹에서 접근할 URL 경로 패턴
        String realPath = "file:" + baseFolderPath + "/";
        // 웹 경로와 파일 시스템 경로를 매핑
        registry.addResourceHandler(webPath).addResourceLocations(realPath);
    }
}
