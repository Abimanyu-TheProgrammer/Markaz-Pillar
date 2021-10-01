package com.markaz.pillar.config.controller;

import com.markaz.pillar.config.controller.model.annotation.ResponseMessage;
import com.markaz.pillar.config.controller.model.response.GenericResponse;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GenericResponseHandler implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return !converterType.equals(StringHttpMessageConverter.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Pattern pattern = Pattern.compile("com\\.markaz\\.pillar.*", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(returnType.getDeclaringClass().getName());
        if(!matcher.matches()) {
            return body;
        }

        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        Method method = returnType.getMethod();

        String message;
        try {
            message = method.getAnnotation(ResponseMessage.class).value();
        } catch (NullPointerException e) {
            message = HttpStatus.valueOf(servletResponse.getStatus()).getReasonPhrase();
        }

        if(body instanceof Page) {
            return GenericResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .statusCode(servletResponse.getStatus())
                    .message(message)
                    .result(body)
                    .total(((Page<?>) body).getTotalElements())
                    .count(((Page<?>) body).getNumberOfElements())
                    .build();
        }
        if(body instanceof Collection) {
            return GenericResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .statusCode(servletResponse.getStatus())
                    .message(message)
                    .result(body)
                    .count(((Collection<?>) body).size())
                    .build();
        } else {
            return GenericResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .statusCode(servletResponse.getStatus())
                    .message(message)
                    .result(body)
                    .build();
        }
    }
}
