package org.delivery.api.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.error.TokenErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.token.business.TokenBusiness;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    public static final String AUTHORIZATION_TOKEN_HEADER = "authorization-token";

    private final TokenBusiness tokenBusiness;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("Authorization Interceptor url = {}", request.getRequestURI());

        // WEB, chrome의 경우 GET, POST, OPTIONS는 pass
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        // html, css, js, png 등을 요청하는 경우는 pass
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        String accessToken = request.getHeader(AUTHORIZATION_TOKEN_HEADER);
        if (accessToken == null) {
            throw new ApiException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }

        Long userId = tokenBusiness.validationAccessToken(accessToken);
        if (userId != null) {
            RequestAttributes requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
            requestContext.setAttribute("userId", userId, RequestAttributes.SCOPE_REQUEST);
            return true;
        }

        throw new ApiException(ErrorCode.BAD_REQUEST, "인증 실패");
    }
}
