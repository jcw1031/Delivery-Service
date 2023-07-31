package org.delivery.api.common.error;

import org.springframework.http.HttpStatus;

public interface ErrorCodeInterface {

    HttpStatus getHttpStatus();

    Integer getErrorCode();

    String getDescription();
}
