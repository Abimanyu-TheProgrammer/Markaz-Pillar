package com.markaz.pillar.volunteer.admin.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class VolunteerProgramRequestDTO {
    @NotBlank
    @Size(max = 256)
    @ToString.Include
    private String name;

    @NotBlank
    @Size(max = 2048)
    private String description;

    @NotBlank
    @Size(max = 2048)
    private String term;

    @NotBlank
    @Size(max = 2048)
    private String benefit;

    @NotNull
    @PositiveOrZero
    private Integer volunteerNeeded;

    @NotBlank
    @Size(max = 2048)
    private String location;

    @NotBlank
    @Size(max = 2048)
    private String schedule;
}
