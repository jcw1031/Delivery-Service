package org.delivery.api.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delivery.api.common.error.ErrorCodeInterface;

import javax.validation.Valid;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Api<T> {

    private Result result;
    @Valid
    private T body;

    public static <T> Api<T> OK(T data) {
        Api<T> api = new Api<T>();
        api.result = Result.OK();
        api.body = data;
        return api;
    }

    public static Api<Object> ERROR(Result result) {
        Api<Object> api = new Api<>();
        api.result = result;
        return api;
    }

    public static Api<Object> ERROR(ErrorCodeInterface errorCode) {
        Api<Object> api = new Api<>();
        api.result = Result.ERROR(errorCode);
        return api;
    }

    public static Api<Object> ERROR(ErrorCodeInterface errorCode, Throwable throwable) {
        Api<Object> api = new Api<>();
        api.result = Result.ERROR(errorCode, throwable);
        return api;
    }

    public static Api<Object> ERROR(ErrorCodeInterface errorCode, String description) {
        Api<Object> api = new Api<>();
        api.result = Result.ERROR(errorCode, description);
        return api;
    }
}
