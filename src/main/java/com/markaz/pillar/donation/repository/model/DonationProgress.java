package com.markaz.pillar.donation.repository.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "donation_progress")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class DonationProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(
            targetEntity = DonationDetail.class,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinColumn(name = "donation_id", nullable = false)
    private DonationDetail donation;

    @NotBlank
    @URL
    @Size(max = 2048)
    @Column(name = "thumbnail_url")
    private String thumbnailURL;

    @NotNull
    @ToString.Include
    @Column(name = "progress_date")
    private LocalDate progressDate;

    @NotBlank
    @Size(max = 2048)
    private String description;
}
