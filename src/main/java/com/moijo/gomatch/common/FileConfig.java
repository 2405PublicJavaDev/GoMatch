package com.moijo.gomatch.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// `Getter`는 Lombok 애노테이션으로, 클래스 필드에 대한 getter 메서드를 자동으로 생성합니다.
// `Value`는 Spring의 애노테이션으로, 환경 설정 값(properties 파일 등)을 주입받기 위해 사용됩니다.
// `Configuration`은 Spring이 이 클래스를 설정 파일로 인식하도록 하는 애노테이션입니다.
// `WebMvcConfigurer`는 웹 설정을 커스터마이징하기 위한 인터페이스로, 정적 리소스 경로나 인터셉터 등의 설정을 추가할 수 있습니다.

@Configuration
@Getter
// `@Configuration`은 Spring 애플리케이션에서 설정 파일 역할을 하며, `@Getter`는 모든 필드에 대한 getter 메서드를 Lombok이 자동으로 생성합니다.

public class FileConfig implements WebMvcConfigurer {
// `FileConfig` 클래스는 `WebMvcConfigurer` 인터페이스를 구현하여 Spring MVC 설정을 확장합니다.

    public static final String FOLDER_PATH = "/gomatch/";
    // 웹에서 사용할 기본 폴더 경로를 상수로 선언합니다. "/gomatch/" 경로는 리소스에 대한 기본 URL 경로로 사용됩니다.

    public static String realFolderPath;
    // 실제 파일이 저장될 서버 내 폴더 경로를 저장할 변수입니다. 이 변수는 정적으로 선언되어 있어 클래스 레벨에서 공유됩니다.

    @Value("${GOMATCH_UPLOAD_FOLDER_PATH}")
    public void setRealFolderPath(String realFolderPath) {
        FileConfig.realFolderPath = realFolderPath;
    }
    // `@Value` 애노테이션을 통해 환경 설정 파일(`application.properties` 또는 `application.yml`)에서 `GOMATCH_UPLOAD_FOLDER_PATH` 값을 주입받습니다.
    // `setRealFolderPath` 메서드를 통해 설정 값이 `realFolderPath`에 저장되며, 이는 실제 서버의 파일 저장 경로입니다.

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String webPath = FOLDER_PATH + "**";
        String realPath = "file:" + realFolderPath;
        registry.addResourceHandler(webPath)
                .addResourceLocations(realPath);
    }
    // `addResourceHandlers` 메서드는 정적 리소스의 URL 경로와 실제 파일 경로를 매핑하는 설정을 추가합니다.
    // `webPath`는 "/gomatch/**"와 같이 웹에서 접근할 URL 패턴을 나타내며, `realPath`는 서버 내부의 실제 경로로, 환경 설정에서 주입된 값 앞에 "file:"을 붙여 로컬 파일 경로를 지정합니다.
    // `registry.addResourceHandler`는 특정 URL 패턴(webPath)에 해당하는 정적 리소스가 실제 서버의 경로(realPath)에 위치하도록 설정합니다.
}