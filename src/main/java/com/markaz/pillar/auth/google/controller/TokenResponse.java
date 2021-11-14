package com.markaz.pillar.auth.google.controller;

import com.markaz.pillar.auth.google.service.model.CredentialResponse;
import com.markaz.pillar.auth.jwt.controller.model.JwtResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {
    private JwtResponse token;
    private CredentialResponse credential;
}
