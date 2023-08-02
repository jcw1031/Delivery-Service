package org.delivery.api.common.exception;

import lombok.Getter;
import org.delivery.api.common.error.ErrorCodeInterface;

@Getter
public class ApiException extends RuntimeException implements ApiExceptionInterface {

    private final ErrorCodeInterface errorCode;
    private final String errorDescription;

    public ApiException(ErrorCodeInterface errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorDescription = errorCode.getDescription();
    }

    public ApiException(ErrorCodeInterface errorCode, String errorDescription) {
        super(errorDescription);
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public ApiException(ErrorCodeInterface errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
        this.errorDescription = errorCode.getDescription();
    }

    public ApiException(ErrorCodeInterface errorCode, Throwable throwable, String errorDescription) {
        super(throwable);
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }
}
