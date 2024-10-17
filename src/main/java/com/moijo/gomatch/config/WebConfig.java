package com.moijo.gomatch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /goods/rep/** 경로를 C:/gomatch/goods/rep/ 디렉토리에 매핑
        registry.addResourceHandler("/goods/rep/**")
                .addResourceLocations("file:///C:/gomatch/goods/rep/");

        // /goods/det/** 경로를 C:/gomatch/goods/det/ 디렉토리에 매핑
        registry.addResourceHandler("/goods/det/**")
                .addResourceLocations("file:///C:/gomatch/goods/det/");
    }
}