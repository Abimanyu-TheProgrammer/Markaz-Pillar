package com.markaz.pillar.volunteer.admin.controller.model;

import com.markaz.pillar.volunteer.repository.model.VolunteerProgram;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class VolunteerProgramSimpleDTO {
    @NotNull
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String slug;

    @NotNull
    private String thumbnailURL;

    @NotNull
    private String description;

    @NotNull
    private Integer volunteerNeeded;

    @NotNull
    private Integer volunteerApplied;

    @NotNull
    private String location;

    public static VolunteerProgramSimpleDTO mapFrom(VolunteerProgram obj) {
        return builder()
                .id(obj.getId())
                .name(obj.getName())
                .slug(obj.getSlug())
                .thumbnailURL(obj.getThumbnailURL())
                .description(obj.getDescription())
                .volunteerNeeded(obj.getVolunteerNeeded())
                .volunteerApplied(obj.getVolunteerApplied())
                .location(obj.getLocation())
                .build();
    }
}
