package com.markaz.pillar.donation.repository.model;

import com.markaz.pillar.markaz.repository.model.Markaz;
import com.markaz.pillar.santri.repository.model.Santri;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "donation_detail")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Where(clause = "is_active = 1")
@SQLDelete(sql = "update donation_detail set is_active = false where id = ?")
public class DonationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(targetEntity = Markaz.class)
    @JoinColumn(name = "markaz_id")
    private Markaz markaz;

    @ElementCollection(targetClass= MarkazDonationCategory.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="markaz_donation_category", joinColumns = @JoinColumn(name = "donation_id"))
    @Column(name="category")
    private Set<MarkazDonationCategory> categories;

    @OneToOne(targetEntity = Santri.class)
    @JoinColumn(name = "santri_id")
    private Santri santri;

    @NotBlank
    @Size(max = 2048)
    private String description;

    @NotNull
    private long nominal;

    @Formula("coalesce((select sum(u.amount) " +
            "from user_donation u " +
            "where u.donation_id = id and u.status = 'DONASI_DITERIMA'" +
            "group by u.donation_id), 0)")
    private Long donated;

    @NotNull
    @Column(name = "is_active")
    private boolean isActive = true;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
