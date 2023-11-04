package com.student.api.annotation.extractor.auth;

import com.nimbusds.jwt.SignedJWT;
import com.student.api.dto.common.enums.Role;
import com.student.api.exception.TokenInfoExtractionException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.student.api.exception.handler.ErrorConstants.FAILED_TO_AUTHENTICATE;

public class AuthInfoResolver implements HandlerMethodArgumentResolver {



    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthInfo.class);
    }

    @Override
    public Info resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        return resolve(request);
    }

    private Info resolve(HttpServletRequest request) throws TokenInfoExtractionException {
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
            Map<String, Object> payload = SignedJWT.parse(token).getPayload().toJSONObject();
            String email = (String) payload.get("email");
            Map<String, Map<String, List<String>>> roles = (Map<String, Map<String, List<String>>>) payload.get("resource_access");

            List<Role> applicationRoles = roles.values().stream()
                    .map(Map::values)
                    .flatMap(Collection::stream)
                    .flatMap(Collection::stream)
                    .map(Role::fromRoleName)
                    .filter(Objects::nonNull)
                    .toList();
            return new Info(email, applicationRoles);
        } catch (Exception e) {
            throw new TokenInfoExtractionException(FAILED_TO_AUTHENTICATE, HttpStatus.UNAUTHORIZED);
        }
    }


}
