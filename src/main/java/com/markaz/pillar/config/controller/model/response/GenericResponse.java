package com.markaz.pillar.config.controller.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse {
    private LocalDateTime timestamp;
    private int statusCode;
    private String message;
    private Object result;
    private Integer count;
    private Long total;
}
