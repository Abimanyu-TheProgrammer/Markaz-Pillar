package com.markaz.pillar.volunteer.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class ProgramRegistrationRequestDTO {
    @NotBlank
    @Size(max = 256)
    @ToString.Include
    private String name;

    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = "^\\d{6}([04][1-9]|[1256][0-9]|[37][01])(0[1-9]|1[0-2])\\d{2}\\d{4}$")
    private String ktp;

    @NotBlank
    @Size(min = 9, max = 30)
    @Pattern(regexp = "^(\\+62|62)?[\\s-]?0?8[1-9]{1}\\d{1}[\\s-]?\\d{4}[\\s-]?\\d{2,5}$")
    @Column(name = "phone_num")
    private String phoneNum;

    @Size(max = 512)
    @NotBlank
    @Email
    @ToString.Include
    private String email;

    @NotBlank
    @Size(max = 2048)
    private String address;
}
