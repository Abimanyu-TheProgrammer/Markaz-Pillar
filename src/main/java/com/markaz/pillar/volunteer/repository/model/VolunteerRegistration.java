package com.markaz.pillar.volunteer.repository.model;

import com.markaz.pillar.auth.repository.models.AuthUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "volunteer_registration",
        indexes = {
                @Index(columnList = "name"),
                @Index(columnList = "status")
        }
)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class VolunteerRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = VolunteerProgram.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "program_id", nullable = false)
    private VolunteerProgram program;

    @ManyToOne(targetEntity = AuthUser.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private AuthUser user;

    @NotBlank
    @Size(max = 256)
    @ToString.Include
    private String name;

    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = "^\\d{6}([04][1-9]|[1256][0-9]|[37][01])(0[1-9]|1[0-2])\\d{2}\\d{4}$")
    private String ktp;

    @NotBlank
    @Size(min = 9, max = 30)
    @Pattern(regexp = "^(\\+62|62)?[\\s-]?0?8[1-9]{1}\\d{1}[\\s-]?\\d{4}[\\s-]?\\d{2,5}$")
    @Column(name = "phone_num")
    private String phoneNum;

    @Size(max = 512)
    @NotBlank
    @Email
    @ToString.Include
    private String email;

    @NotBlank
    @Size(max = 2048)
    private String address;

    @Size(max = 2048)
    @URL
    @Column(name = "picture_url")
    private String pictureURL;

    @Size(max = 2048)
    @URL
    @Column(name = "essay_url")
    private String essayURL;

    @Size(max = 2048)
    @URL
    @Column(name = "cv_url")
    private String cvURL;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RegistrationStatus status = RegistrationStatus.MENUNGGU_KONFIRMASI;

    @Size(max = 2048)
    private String reason;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
}
