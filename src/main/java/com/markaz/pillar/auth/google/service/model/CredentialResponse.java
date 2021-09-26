package com.markaz.pillar.auth.google.service.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
public class CredentialResponse {
    @JsonAlias("sub")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;

    private String name;

    @JsonAlias("given_name")
    private String givenName;

    @JsonAlias("family_name")
    private String familyName;

    @URL
    private String picture;

    @Email
    private String email;

    @JsonAlias("email_verified")
    private boolean emailVerified;

    private String locale;

    private String hd;
}
