package com.moijo.gomatch.common;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

// 필요한 클래스와 인터페이스를 가져오고 있습니다. `ServletRequest`, `ServletResponse`, `FilterChain` 등은 서블릿 필터와 관련된 클래스들입니다.
// `HttpServletRequest`, `HttpServletResponse`는 HTTP 요청과 응답을 처리하는 데 사용됩니다.
// `HttpSession`은 사용자 세션을 관리하는 클래스이고, `Slf4j`는 로그를 찍기 위한 Lombok 애노테이션입니다.

@WebFilter(urlPatterns = "/*")
// `WebFilter` 애노테이션은 이 필터가 어떤 경로에 대해 작동할지를 정의합니다. 여기서는 모든 URL에 대해 필터가 적용되도록 "/*"로 설정했습니다.

@Slf4j
// `@Slf4j`는 Lombok의 애노테이션으로, 로그를 남길 수 있는 `log` 객체를 자동으로 생성합니다.

public class AuthenticationFilter implements Filter {
// `Filter` 인터페이스를 구현한 `AuthenticationFilter` 클래스입니다. 필터는 HTTP 요청이 서버에 도달하기 전에 특정 로직을 실행하는 데 사용됩니다.

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("Authentication Filter 생성");
    }
    // `init` 메서드는 필터가 초기화될 때 호출됩니다. 여기서는 초기화 로그를 남기고 있습니다.

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // `doFilter` 메서드는 필터의 핵심 메서드로, 각 요청이 필터를 통과할 때마다 실행됩니다.
        // `ServletRequest`와 `ServletResponse` 객체를 각각 `HttpServletRequest`와 `HttpServletResponse`로 캐스팅하고 있습니다.

        HttpSession session = request.getSession();
        // 현재 요청에 대한 세션 객체를 가져옵니다. 세션은 사용자의 로그인 정보 등을 저장할 수 있는 공간입니다.

        String uri = request.getRequestURI();
        // 현재 요청된 URL의 경로를 가져옵니다.

        // /admin/login URL을 무시
        if (uri.startsWith("/admin/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 만약 요청된 URL이 "/admin/login"으로 시작한다면, 필터를 더 이상 처리하지 않고 다음 필터로 넘깁니다. 즉, 로그인 페이지는 필터링하지 않습니다.
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.info("Authentication Filter 파괴");
    }
    // `destroy` 메서드는 필터가 제거될 때 호출됩니다. 여기서는 필터가 파괴되었음을 로그로 남깁니다.
}