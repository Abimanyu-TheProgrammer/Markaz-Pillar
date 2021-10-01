package com.markaz.pillar.auth.jwt.controller.model;

import com.markaz.pillar.config.validation.ValidPassword;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class JwtRequest implements Serializable {
    @NotBlank
    private String email;

    @NotBlank
    @ValidPassword
    private String password;
}