package com.markaz.pillar.auth.google.repository.addon;

import com.markaz.pillar.auth.google.repository.model.GoogleToken;
import com.markaz.pillar.auth.repository.models.AuthUser;

public interface GoogleTokenAddOn {
    GoogleToken refreshToken(AuthUser user);
    GoogleToken createToken(String state, String code);
}
