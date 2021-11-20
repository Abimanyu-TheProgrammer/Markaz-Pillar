package com.markaz.pillar.volunteer.admin.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class TestimonyRequestDTO {
    @NotBlank
    @Size(max = 256)
    @ToString.Include
    private String name;

    @NotBlank
    @Size(max = 2048)
    private String description;
}
