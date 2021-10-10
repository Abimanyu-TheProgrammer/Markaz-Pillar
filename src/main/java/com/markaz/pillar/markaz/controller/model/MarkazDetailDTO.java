package com.markaz.pillar.markaz.controller.model;

import com.markaz.pillar.donation.repository.model.MarkazDonationCategory;
import com.markaz.pillar.markaz.repository.model.Markaz;
import com.markaz.pillar.markaz.repository.model.MarkazCategory;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class MarkazDetailDTO {
    @NotNull
    private Integer id;

    @NotNull
    @ToString.Include
    private String name;

    @NotNull
    private String slug;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MarkazCategory category;

    @NotNull
    private String background;

    @NotNull
    private String thumbnailURL;

    @NotNull
    private String address;

    @NotEmpty
    private Set<MarkazDonationCategory> donationCategories;

    private String description;
    private Long nominal;
    private Long donated;
    private String contactPerson;

    public static MarkazDetailDTO mapFrom(Markaz obj) {
        MarkazDetailDTOBuilder builder = builder()
                .id(obj.getId())
                .name(obj.getName())
                .slug(obj.getSlug())
                .category(obj.getCategory())
                .background(obj.getBackground())
                .thumbnailURL(obj.getThumbnailURL())
                .address(obj.getAddress());

        if(obj.getDonationDetail() != null) {
            builder = builder.donationCategories(obj.getDonationDetail().getCategories())
                    .description(obj.getDonationDetail().getDescription())
                    .nominal(obj.getDonationDetail().getNominal())
                    .donated(obj.getDonationDetail().getDonated())
                    .contactPerson(obj.getDonationDetail().getContactPerson());
        }

        return builder.build();
    }
}
