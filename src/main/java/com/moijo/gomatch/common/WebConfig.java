package com.moijo.gomatch.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/** 경로로 요청이 들어오면 C:/gomatch/board/toast/ 경로에 있는 파일을 제공하도록 설정
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///C:/gomatch/board/toast/");
    }
}
