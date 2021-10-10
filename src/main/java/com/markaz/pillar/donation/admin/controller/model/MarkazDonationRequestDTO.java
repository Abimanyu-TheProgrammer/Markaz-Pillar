package com.markaz.pillar.donation.admin.controller.model;

import com.markaz.pillar.donation.repository.model.MarkazDonationCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
public class MarkazDonationRequestDTO {
    @NotBlank
    private String contactPerson;

    @NotEmpty
    private Set<MarkazDonationCategory> categories;

    @NotBlank
    private String description;

    @NotNull
    private Long nominal;
}
