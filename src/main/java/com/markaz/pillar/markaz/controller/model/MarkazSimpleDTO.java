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

    @NotNull
    private String contactName;

    @NotNull
    private String contactInfo;

    public static MarkazSimpleDTO mapFrom(Markaz obj) {
        MarkazSimpleDTOBuilder builder = builder()
                .id(obj.getId())
                .slug(obj.getSlug())
                .name(obj.getName())
                .category(obj.getCategory())
                .background(obj.getBackground())
                .thumbnailURL(obj.getThumbnailURL())
                .contactName(obj.getContactName())
                .contactInfo(obj.getContactInfo());

        return builder.build();
    }
}
