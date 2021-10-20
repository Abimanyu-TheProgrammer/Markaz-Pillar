package com.markaz.pillar.donation.controller.admin.model;

import com.markaz.pillar.donation.repository.model.DonationDetail;
import com.markaz.pillar.donation.repository.model.MarkazDonationCategory;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class AdminDonationDTO {
    @NotNull
    @ToString.Include
    private String uniqueId;

    @NotNull
    private String name;

    private Set<MarkazDonationCategory> categories;

    @NotNull
    private String description;

    @NotNull
    private Long nominal;

    @NotNull
    private Long donated;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private Boolean isActive;

    public static AdminDonationDTO mapFrom(DonationDetail obj) {
        return AdminDonationDTO.builder()
                .uniqueId(obj.getUniqueId())
                .name(obj.getName())
                .categories(obj.getCategories().isEmpty() ? null : obj.getCategories())
                .description(obj.getDescription())
                .nominal(obj.getNominal())
                .donated(obj.getDonated())
                .createdAt(obj.getCreatedAt())
                .isActive(obj.isActive())
                .build();
    }
}
