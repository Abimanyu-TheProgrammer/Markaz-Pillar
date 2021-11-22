package com.markaz.pillar.volunteer.admin.controller.model;

import com.markaz.pillar.volunteer.repository.model.ProgramTestimony;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class TestimonyDTO {
    @NotNull
    private Integer id;

    @NotNull
    private String thumbnailURL;

    @NotNull
    private String name;

    @NotNull
    private String description;

    public static TestimonyDTO mapFrom(ProgramTestimony obj) {
        return builder()
                .id(obj.getId())
                .name(obj.getName())
                .thumbnailURL(obj.getThumbnailURL())
                .description(obj.getDescription())
                .build();
    }
}
