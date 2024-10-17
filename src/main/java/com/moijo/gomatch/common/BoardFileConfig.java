package com.moijo.gomatch.common;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.Serializable;

@Configuration
@Getter
public class BoardFileConfig implements WebMvcConfigurer{

    private static String baseFolderPath;
    private static String boardFolderPath;

    @Value("${GOMATCH_UPLOAD_BASE_PATH}")
    public void setBaseFolderPath(String baseFolderPath){
        BoardFileConfig.baseFolderPath = baseFolderPath;
        // board 폴더 경로를 설정
        BoardFileConfig.boardFolderPath = baseFolderPath + "/";
    }

    public static String getModuleUploadPath(String moduleName){
        return boardFolderPath + moduleName + "/";
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String webPath = "/uploads/**";
        String realPath = "file:" + boardFolderPath;
        registry.addResourceHandler(webPath).addResourceLocations(realPath);
    }
}
