package com.markaz.pillar.volunteer.repository.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "volunteer_testimony")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class ProgramTestimony {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = VolunteerProgram.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "program_id", nullable = false)
    private VolunteerProgram program;

    @NotBlank
    @URL
    @Size(max = 2048)
    @Column(name = "thumbnail_url")
    private String thumbnailURL;

    @NotBlank
    @Size(max = 256)
    @ToString.Include
    private String name;

    @NotBlank
    @Size(max = 2048)
    private String description;
}
