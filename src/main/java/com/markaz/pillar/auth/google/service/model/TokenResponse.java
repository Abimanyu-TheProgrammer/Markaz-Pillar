package com.markaz.pillar.auth.google.service.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenResponse {
    @JsonAlias("access_token")
    private String accessToken;

    @JsonAlias("refresh_token")
    private String refreshToken;

    private String scope;

    @JsonAlias("expires_in")
    private int expiresIn;

    @JsonAlias("token_type")
    private String tokenType;

    @JsonAlias("id_token")
    private String idToken;
}
