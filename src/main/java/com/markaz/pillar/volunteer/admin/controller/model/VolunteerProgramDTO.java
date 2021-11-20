package com.markaz.pillar.volunteer.admin.controller.model;

import com.markaz.pillar.volunteer.repository.model.VolunteerProgram;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class VolunteerProgramDTO {
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

    @NotNull
    private String term;

    @NotNull
    private String benefit;

    @NotNull
    private String schedule;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private List<TestimonyDTO> testimonies;

    public static VolunteerProgramDTO mapFrom(VolunteerProgram obj) {
        return builder()
                .id(obj.getId())
                .name(obj.getName())
                .slug(obj.getSlug())
                .thumbnailURL(obj.getThumbnailURL())
                .description(obj.getDescription())
                .volunteerNeeded(obj.getVolunteerNeeded())
                .volunteerApplied(obj.getVolunteerApplied())
                .location(obj.getLocation())
                .testimonies(
                        obj.getTestimonies().stream()
                                .map(TestimonyDTO::mapFrom)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
