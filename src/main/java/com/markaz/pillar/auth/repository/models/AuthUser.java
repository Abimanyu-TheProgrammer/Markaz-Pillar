package com.markaz.pillar.auth.repository.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.markaz.pillar.auth.google.repository.model.GoogleToken;
import com.markaz.pillar.config.validation.ValidPassword;
import com.markaz.pillar.donation.repository.model.UserDonation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "auth_user",
        uniqueConstraints = @UniqueConstraint(columnNames={"username", "email"})
)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(targetEntity = Role.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "account")
    @Where(clause = "is_active = 1")
    @JsonIgnore
    private GoogleToken token;

    @Size(max = 512)
    @NotBlank
    @Email
    @ToString.Include
    private String email;

    @Size(max = 64)
    @NotBlank
    @ToString.Include
    private String username;

    @Size(max = 512)
    @ValidPassword
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Size(max = 512)
    @NotBlank
    @Column(name = "full_name")
    private String fullName;

    @Size(max = 2048)
    @URL
    @Column(name = "profile_url")
    private String profileURL;

    @Size(max = 2048)
    private String description;

    @NotBlank
    @Size(min = 9, max = 30)
    @Pattern(regexp = "^(\\+62|62)?[\\s-]?0?8[1-9]{1}\\d{1}[\\s-]?\\d{4}[\\s-]?\\d{2,5}$")
    @Column(name = "phone_num")
    private String phoneNum;

    @NotBlank
    private String address;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<UserDonation> donations;
}
