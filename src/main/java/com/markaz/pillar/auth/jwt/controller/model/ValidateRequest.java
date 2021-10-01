package com.markaz.pillar.auth.jwt.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
public class ValidateRequest {
    @NotBlank
    private String token;
    private List<String> authority;
}
