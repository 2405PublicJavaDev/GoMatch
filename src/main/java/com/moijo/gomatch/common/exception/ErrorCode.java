package com.moijo.gomatch.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE("C001", "입력 폼이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("C002", "서버 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_VALUE("C003", "중복값을 입력했습니다", HttpStatus.BAD_REQUEST),
    NO_UPDATE("C004", "DML 이 이루어진 row 가 한개도 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND("C005", "404 NOT FOUND", HttpStatus.NOT_FOUND),
    // Member
    LOGIN_FAIL("M001", "로그인에 실패했습니다", HttpStatus.UNAUTHORIZED),
    EMAIL_VALID_FAIL("M002", "이메일 인증에 실패했습니다", HttpStatus.BAD_REQUEST),
    ID_FIND_FAIL("M003", "아이디 찾기에 실패했습니다", HttpStatus.BAD_REQUEST),
    PW_FIND_FAIL("M004", "비밀번호 찾기에 실패했습니다", HttpStatus.BAD_REQUEST),
    PW_PW_CHECK_NOT_MATCH("M005", "비밀번호와 비밀번호 확인이 다릅니다", HttpStatus.BAD_REQUEST)
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(final String code, final String message, final HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
