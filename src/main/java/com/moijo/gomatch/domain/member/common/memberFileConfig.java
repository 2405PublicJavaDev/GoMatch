package com.moijo.gomatch.domain.member.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class memberFileConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/member-img/**")
                .addResourceLocations("classpath:/static/img/member-img/")
                .setCachePeriod(3600)
                .resourceChain(true);
    }
}
