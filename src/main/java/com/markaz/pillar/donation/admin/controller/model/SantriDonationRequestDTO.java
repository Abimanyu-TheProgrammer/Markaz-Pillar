package com.markaz.pillar.donation.admin.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class SantriDonationRequestDTO {
    @NotBlank
    private String description;

    @NotNull
    private Long nominal;
}
