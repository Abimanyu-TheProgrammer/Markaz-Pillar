package com.markaz.pillar.auth.jwt.controller.model;

import lombok.Data;

@Data
public class JwtResponse {
    private final String accessToken;
    private final String refreshToken;
}
