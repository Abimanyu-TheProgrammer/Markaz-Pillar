package com.markaz.pillar.auth.controller.model;

import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.config.validation.ValidPassword;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class RegistrationRequestDTO {
    @Email
    @NotBlank
    @Size(max = 512)
    @ToString.Include
    private String email;

    @Size(max = 64)
    @NotBlank
    private String username;

    @Size(max = 512)
    @NotBlank
    private String fullName;

    @NotBlank
    @Size(min = 9, max = 30)
    @Pattern(regexp = "^(\\+62|62)?[\\s-]?0?8[1-9]{1}\\d{1}[\\s-]?\\d{4}[\\s-]?\\d{2,5}$")
    private String phoneNum;

    @NotBlank
    private String address;

    @Size(max = 512)
    @ValidPassword
    private String password;

    public AuthUser mapTo() {
        AuthUser obj = new AuthUser();
        obj.setEmail(this.getEmail());
        obj.setUsername(this.getUsername());
        obj.setFullName(this.getFullName());
        obj.setPhoneNum(this.getPhoneNum());
        obj.setAddress(this.getAddress());
        obj.setPassword(this.getPassword());

        return obj;
    }
}
