package com.markaz.pillar.santri.controller.model;

import com.markaz.pillar.santri.repository.model.Santri;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class SantriSimpleDTO {
    @NotNull
    private Integer id;

    @NotNull
    private String slug;

    @NotNull
    private String name;

    @NotNull
    private String thumbnailURL;

    @NotNull
    private String background;

    public static SantriSimpleDTO mapFrom(Santri obj) {
        return builder()
                .id(obj.getId())
                .slug(obj.getSlug())
                .name(obj.getName())
                .background(obj.getBackground())
                .thumbnailURL(obj.getThumbnailURL())
                .build();
    }
}
