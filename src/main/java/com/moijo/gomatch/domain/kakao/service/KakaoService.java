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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
    private String redirectUri;

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

    //Webclient로 HTTP 요청을 구현했음
    //각 서비스마다 Custom Exception이 있다면 onStatus 메서드에 지정하면 된다
    //HTTP 요청을 받아오면 .retrieve() 메서드 부터, Request Body 내용이
    //미리 지정해둔 KakaoTokenResponseDto에 json이 직렬화되어 들어가게 된다.

    /**
     * 담당자: 이용재
     * 관련 기능: 카카오 인증 서버에 액세스 토큰 요청
     * 설명: 카카오 인증 서버에 액세스 토큰을 요청하고
     *       인증 코드를 사용해서 액세스 토큰, 리프레시 토큰을 받아오는 역할을 함
     */
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

    /**
     * 담당자: 이용재
     * 관련 기능: 카카오 로그인 프로세스 처리
     * 설명: 로그인 프로세스 처리
     *       액세스 토큰을 얻고, 사용자 정보를 가져온 후 ,JWT 토큰을 생성함
     */
    public KakaoLoginDto processKakaoLogin(String code) {
        String accessToken = getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = getUserInfo(accessToken);

        String jwtToken = generateJwtToken(userInfo);

        return KakaoLoginDto.builder()
                .accessToken(jwtToken)
                .refreshToken(accessToken)
                .nickname(userInfo.getKakaoAccount().getProfile().getNickName())
                .build();
    }

    /**
     * 담당자: 이용재
     * 관련 기능:  카카오 로그인 정보 조회
     * 설명: 카카오 API를 통해 사용자 정보 조회
     *       액세스 토큰을 사용해서 사용자 프로필, 이메일 등 정보를 가져옴
     */
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

    /**
     * 담당자: 이용재
     * 관련 기능: 카카오 사용자 정보 바탕으로 JWT 토큰 생성
     * 설명: 카카오 사용자 정보를 바탕으로 JWT토큰을 생성하고
     *       토큰으로 사용자 이메일,ID, 닉네임, 이름 등의 정보가 포함됩니다
     */
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

    /**
     * 담당자: 이용재
     * 관련 기능: 카카오 로그인 URL를 생성
     * 설명: 클라이언트 ID와 리다이렉트 URI를 포함한 카카오 인증 URL로 반환함
     */
    public String getKakaoLoginUrl() {
        return KAUTH_TOKEN_URL_HOST + "/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";
    }

    /**
     * 담당자: 이용재
     * 관련 기능: 카카오 연동 해제
     * 설명: 액세스 토큰을 사용하여 카카오 API연동 해제 함
     */
    public boolean unlinkKakaoAccount(String accessToken) {
        String unlinkUrl = "https://kapi.kakao.com/v1/user/unlink";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(unlinkUrl, HttpMethod.POST, entity, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Error unlinking Kakao account", e);
            return false;
        }
    }


}
