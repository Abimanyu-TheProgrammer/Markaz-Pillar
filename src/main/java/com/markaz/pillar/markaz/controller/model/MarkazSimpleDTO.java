package com.markaz.pillar.markaz.controller.model;

import com.markaz.pillar.markaz.repository.model.Markaz;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

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
    private String background;

    public static MarkazSimpleDTO mapFrom(Markaz obj) {
        return builder()
                .id(obj.getId())
                .slug(obj.getSlug())
                .name(obj.getName())
                .background(obj.getBackground())
                .thumbnailURL(obj.getThumbnailURL())
                .build();
    }
}
