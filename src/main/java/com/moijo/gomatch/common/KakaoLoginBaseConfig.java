package com.moijo.gomatch.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class KakaoLoginBaseConfig implements WebMvcConfigurer {

    // 사이트 헤더 세션 상속
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new KakaoLoginInterceptor());
    }

    public static class KakaoLoginInterceptor implements HandlerInterceptor {
        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
            if (modelAndView != null) {
                HttpSession session = request.getSession();
                boolean loggedIn = session.getAttribute("loggedIn") != null && (Boolean) session.getAttribute("loggedIn");
                modelAndView.addObject("loggedIn", loggedIn);
                if (loggedIn) {
                    modelAndView.addObject("memberNickName", session.getAttribute("memberNickName"));
                }
            }
        }
    }
}