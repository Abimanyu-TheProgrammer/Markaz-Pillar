package com.markaz.pillar.admin.controller.model;

import com.markaz.pillar.auth.repository.models.AuthUser;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class AuthUserDTO {
    @NotNull
    private Integer id;

    @NotNull
    private String email;

    @NotNull
    private String username;

    @NotNull
    private String fullName;

    @NotNull
    private String phoneNum;

    @NotNull
    private String address;

    public static AuthUserDTO mapFrom(AuthUser obj) {
        return builder()
                .id(obj.getId())
                .email(obj.getEmail())
                .username(obj.getUsername())
                .phoneNum(obj.getPhoneNum())
                .address(obj.getAddress())
                .build();
    }
}
