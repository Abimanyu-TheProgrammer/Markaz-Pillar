package com.markaz.pillar.auth.google.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class TokenRequest {
    @NotBlank
    private String code;
}
