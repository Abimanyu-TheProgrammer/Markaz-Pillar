package com.markaz.pillar.donation.controller.admin.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class DonationProgressRequestDTO {
    @NotNull
    @ToString.Include
    private LocalDate progressDate;

    @NotBlank
    @Size(max = 2048)
    private String description;
}
