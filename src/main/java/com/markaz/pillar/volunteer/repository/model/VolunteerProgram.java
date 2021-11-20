package com.markaz.pillar.volunteer.repository.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "volunteer_program",
        indexes = {
                @Index(columnList = "name", unique = true),
                @Index(columnList = "slug", unique = true)
        }
)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Where(clause = "is_active = 1")
@SQLDelete(sql = "update volunteer_program set is_active = false where id = ?")
public class VolunteerProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(max = 256)
    @ToString.Include
    private String name;

    @NotBlank
    @Size(max = 256)
    private String slug;

    @NotBlank
    @URL
    @Size(max = 2048)
    @Column(name = "thumbnail_url")
    private String thumbnailURL;

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
    @Column(name = "volunteer_needed")
    private Integer volunteerNeeded;

    @Formula("coalesce((select count(t.id) " +
            "from volunteer_registration t " +
            "where t.program_id = id and t.status = 'PENDAFTARAN_DITERIMA'" +
            "group by t.program_id), 0)")
    private Integer volunteerApplied;

    @NotBlank
    @Size(max = 2048)
    private String location;

    @NotBlank
    @Size(max = 2048)
    private String schedule;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @NotNull
    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToMany(mappedBy = "program", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ProgramTestimony> testimonies = new HashSet<>();

    @OneToMany(mappedBy = "program", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<VolunteerRegistration> registrations = new HashSet<>();
}
