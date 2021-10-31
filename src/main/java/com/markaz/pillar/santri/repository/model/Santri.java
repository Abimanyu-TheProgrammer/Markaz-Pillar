package com.markaz.pillar.santri.repository.model;

import com.markaz.pillar.donation.repository.model.DonationDetail;
import com.markaz.pillar.markaz.repository.model.Markaz;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(
        name = "santri_data",
        indexes = {
                @Index(columnList = "name", unique = true),
                @Index(columnList = "slug", unique = true),
                @Index(columnList = "birth_date")
        }
)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Where(clause = "is_active = 1")
@SQLDelete(sql = "update santri_data set is_active = false where id = ?")
public class Santri {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = Markaz.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "markaz_id", nullable = false)
    private Markaz markaz;

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
    private String background;

    @NotBlank
    @Size(max = 2048)
    private String address;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotBlank
    @Size(max = 128)
    @Column(name = "birth_place")
    private String birthPlace;

    @NotNull
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotNull
    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToMany(mappedBy = "santri", orphanRemoval = true, cascade = CascadeType.ALL)
    @Where(clause = "is_active = 1")
    private List<DonationDetail> donationDetails = new ArrayList<>();
}
