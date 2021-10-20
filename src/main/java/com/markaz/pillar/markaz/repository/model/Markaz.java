package com.markaz.pillar.markaz.repository.model;

import com.markaz.pillar.donation.repository.model.DonationDetail;
import com.markaz.pillar.markaz.repository.model.constraint.EmailOrPhone;
import com.markaz.pillar.santri.repository.model.Santri;
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
import java.util.Set;

@Entity
@Table(
        name = "markaz_data",
        indexes = {
                @Index(columnList = "name", unique = true),
                @Index(columnList = "slug", unique = true),
                @Index(columnList = "category")
        }
)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SQLDelete(sql = "update markaz_data set is_active = false where id = ?")
public class Markaz {
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

    @NotNull
    @Enumerated(EnumType.STRING)
    private MarkazCategory category;

    @NotBlank
    @Size(max = 2048)
    private String background;

    @NotBlank
    @URL
    @Size(max = 2048)
    @Column(name = "thumbnail_url")
    private String thumbnailURL;

    @NotBlank
    @Size(max = 2048)
    private String address;

    @NotBlank
    @Size(max = 512)
    @Column(name = "contact_name")
    private String contactName;

    @NotBlank
    @Size(max = 512)
    @Column(name = "contact_info")
    @EmailOrPhone
    private String contactInfo;

    @NotNull
    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToMany(mappedBy = "markaz", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Santri> santri;

    @OneToOne(mappedBy = "markaz", orphanRemoval = true, cascade = CascadeType.ALL)
    @Where(clause = "is_active = 1")
    private DonationDetail donationDetail;
}
