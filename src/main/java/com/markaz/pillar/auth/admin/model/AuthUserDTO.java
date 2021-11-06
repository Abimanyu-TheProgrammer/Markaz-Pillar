package com.markaz.pillar.auth.admin.model;

import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.auth.repository.models.Menu;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class AuthUserDTO {
    @NotNull
    private Integer id;

    @NotNull
    private String email;

    @NotNull
    private String role;

    @NotNull
    @NotEmpty
    private Set<String> menus;

    @NotNull
    private String username;

    @NotNull
    private String fullName;

    @NotNull
    private String phoneNum;

    @NotNull
    private String address;

    private String profileURL;

    private String description;

    public static AuthUserDTO mapFrom(AuthUser obj) {
        return builder()
                .id(obj.getId())
                .email(obj.getEmail())
                .username(obj.getUsername())
                .role(obj.getRole().getName())
                .menus(
                        obj.getRole().getMenus()
                                .stream()
                                .map(Menu::getName)
                                .collect(Collectors.toSet())
                )
                .phoneNum(obj.getPhoneNum())
                .address(obj.getAddress())
                .profileURL(obj.getProfileURL())
                .description(obj.getDescription())
                .build();
    }
}
