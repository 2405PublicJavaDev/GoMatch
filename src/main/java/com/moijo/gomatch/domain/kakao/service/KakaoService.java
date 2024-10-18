package com.moijo.gomatch.domain.kakao.service;

import com.moijo.gomatch.domain.kakao.dto.KakaoLoginDto;
import com.moijo.gomatch.domain.kakao.dto.KakaoTokenResponseDto;
import com.moijo.gomatch.domain.kakao.dto.KakaoUserInfoResponseDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private final String  KAUTH_TOKEN_URL_HOST;
    private final String  KAUTH_USER_URL_HOST;

    @Autowired
    public KakaoService(@Value("${kakao.client_id}") String clientId) {
        this.clientId = clientId;
        KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
        KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
    }

    //Webclient로 HTTP 요청을 구현했다.
    //각 서비스마다 Custom Exception이 있다면 onStatus 메서드에 지정하면 되겠다.
    //HTTP 요청을 받아오면 .retrieve() 메서드 부터, Request Body 내용이
    //미리 지정해둔 KakaoTokenResponseDto에 json이 직렬화되어 들어가게 된다.

    // 액세스 토큰 받기
    public String getAccessTokenFromKakao(String code) {
        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();


        log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
        //제공 조건: OpenID Connect가 활성화 된 앱의 토큰 발급 요청인 경우 또는 scope에 openid를 포함한 추가 항목 동의 받기 요청을 거친 토큰 발급 요청인 경우
        log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
        log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());

        return kakaoTokenResponseDto.getAccessToken();
    }
    public KakaoLoginDto processKakaoLogin(String code) {
        String accessToken = getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = getUserInfo(accessToken);

        String jwtToken = generateJwtToken(userInfo);

        return KakaoLoginDto.builder()
                .accessToken(jwtToken)
                .refreshToken(accessToken) // Kakao access token을 refresh token으로 사용
                .build();
    }

    // 사용자 정보
    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {

        KakaoUserInfoResponseDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        log.info("[ Kakao Service ] Auth ID ---> {} ", userInfo.getId());
        log.info("[ Kakao Service ] NickName ---> {} ", userInfo.getKakaoAccount().getProfile().getNickName());
        log.info("[ Kakao Service ] profile ---> {} ", userInfo.getKakaoAccount().getProfile());
        log.info("[ Kakao Service ] name ---> {} ", userInfo.getKakaoAccount().getName());
        log.info("[ Kakao Service ] email ---> {} ", userInfo.getKakaoAccount().getEmail());
        log.info("[ Kakao Service ] birthday ---> {} ", userInfo.getKakaoAccount().getBirthDay());
        log.info("[ Kakao Service ] birthyear ---> {} ", userInfo.getKakaoAccount().getBirthYear());


        return userInfo;
    }
    private String generateJwtToken(KakaoUserInfoResponseDto userInfo) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(userInfo.getKakaoAccount().getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .addClaims(Map.of(
                        "id", userInfo.getId(),
                        "nickname", userInfo.getKakaoAccount().getProfile().getNickName(),
                        "name", userInfo.getKakaoAccount().getName()
                ))
                .signWith(key)
                .compact();
    }

    public String getKakaoLoginUrl() {
        return KAUTH_TOKEN_URL_HOST + "/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirect_uri
                + "&response_type=code";
    }


//    {"loginSuccess":true,"token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJla3N0bnNna2VrcmgxQGdtYWlsLmNvbSIsImlhdCI6MTcyOTIxNzAxNSwiZXhwIjoxNzI5MzAzNDE1LCJuYW1lIjoi7J207Jqp7J6sIiwibmlja25hbWUiOiLsnbTsmqnsnqwiLCJpZCI6Mzc1NDI0MjkxOH0.uyPIXbF9_adTMMDc5wM2muCgQAKprVuUsojKoEjwdxs","refreshToken":"XJPSjLiDF6KMPruwe3No3Umr6uvnbvPgAAAAAQoqJZEAAAGSnV2ukP6hmr4nKm-b"}
}
