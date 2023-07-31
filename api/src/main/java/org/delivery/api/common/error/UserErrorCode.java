package org.delivery.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCodeInterface {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 1404, "사용자를 찾을 수 없음.");

    private final HttpStatus httpStatus;
    private final Integer errorCode;
    private final String description;
}
