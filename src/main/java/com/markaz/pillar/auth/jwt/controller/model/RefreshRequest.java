package com.markaz.pillar.auth.jwt.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefreshRequest {
    private String accessToken;
    private String refreshToken;
}
