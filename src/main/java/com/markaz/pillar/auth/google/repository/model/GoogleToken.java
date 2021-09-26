package com.markaz.pillar.auth.google.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.markaz.pillar.auth.repository.models.AuthUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "google_token")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class GoogleToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(targetEntity = AuthUser.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Include
    private AuthUser account;

    @Size(max = 30)
    @Column(unique = true)
    private String state;

    @Size(max = 2048)
    @Column(name = "access_token")
    @JsonIgnore
    private String accessToken;

    @Size(max = 2048)
    @Column(name = "refresh_token")
    @JsonIgnore
    private String refreshToken;

    @Size(max = 4096)
    @Column(name = "id_token")
    @JsonIgnore
    private String idToken;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expire_at", nullable = false, updatable = false)
    private LocalDateTime expireAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}
