package org.delivery.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum TokenErrorCode implements ErrorCodeInterface {

    INVALID_TOKEN(HttpStatus.BAD_REQUEST, 2000, "유효하지 않은 토큰"),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, 2001, "만료된 토큰"),
    TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, 2002, "토큰 알 수 없는 에러"),
    AUTHORIZATION_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, 2003, "인증 헤더 토큰 없음");

    private final HttpStatus httpStatus;
    private final Integer errorCode;
    private final String description;
}
