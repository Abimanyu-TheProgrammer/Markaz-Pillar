package com.markaz.pillar.auth.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class EditProfileRequestDTO {
    @Size(max = 512)
    private String fullName;

    @Size(min = 9, max = 30)
    @Pattern(regexp = "^(\\+62|62)?[\\s-]?0?8[1-9]{1}\\d{1}[\\s-]?\\d{4}[\\s-]?\\d{2,5}$")
    private String phoneNum;

    private String address;

    @Size(max = 2048)
    private String description;
}
