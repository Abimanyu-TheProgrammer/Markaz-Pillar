package com.markaz.pillar.donation.controller.admin.model;

import com.markaz.pillar.donation.repository.model.MarkazDonationCategory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class MarkazDonationRequestDTO {
    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    private Set<MarkazDonationCategory> categories;

    @NotBlank
    @Size(max = 2048)
    private String description;

    @NotNull
    private Long nominal;

    @NotNull
    private Boolean isActive;
}
