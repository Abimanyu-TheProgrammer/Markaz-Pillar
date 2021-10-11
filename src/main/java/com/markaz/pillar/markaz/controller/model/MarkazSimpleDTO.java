package com.markaz.pillar.markaz.controller.model;

import com.markaz.pillar.markaz.repository.model.Markaz;
import com.markaz.pillar.markaz.repository.model.MarkazCategory;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class MarkazSimpleDTO {
    @NotNull
    private Integer id;

    @NotNull
    private String slug;

    @NotNull
    private String name;

    @NotNull
    private String thumbnailURL;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MarkazCategory category;

    @NotNull
    private String background;

    private Long nominal;
    private String contactPerson;

    public static MarkazSimpleDTO mapFrom(Markaz obj) {
        MarkazSimpleDTOBuilder builder = builder()
                .id(obj.getId())
                .slug(obj.getSlug())
                .name(obj.getName())
                .background(obj.getBackground())
                .thumbnailURL(obj.getThumbnailURL());

        if(obj.getDonationDetail() != null) {
            builder.nominal(obj.getDonationDetail().getNominal())
                    .contactPerson(obj.getDonationDetail().getContactPerson());
        }

        return builder.build();
    }
}
