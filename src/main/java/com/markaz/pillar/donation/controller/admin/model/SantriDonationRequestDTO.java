package com.markaz.pillar.donation.controller.admin.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class SantriDonationRequestDTO {
    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 2048)
    private String description;

    @NotNull
    private Long nominal;

    @NotNull
    private Boolean isActive;
}
