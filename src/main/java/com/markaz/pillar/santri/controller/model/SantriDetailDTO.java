package com.markaz.pillar.santri.controller.model;

import com.markaz.pillar.donation.controller.admin.model.DonationProgressDTO;
import com.markaz.pillar.donation.repository.model.DonationDetail;
import com.markaz.pillar.markaz.controller.model.MarkazDetailDTO;
import com.markaz.pillar.santri.repository.model.Gender;
import com.markaz.pillar.santri.repository.model.Santri;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class SantriDetailDTO {
    @NotNull
    private Integer id;

    @NotNull
    private String donationId;

    @NotNull
    private MarkazDetailDTO markaz;

    @NotNull
    @ToString.Include
    private String name;

    @NotNull
    private String slug;

    @NotNull
    private String background;

    @NotNull
    private String thumbnailURL;

    @NotNull
    private String address;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    private String birthPlace;

    @NotNull
    private LocalDate birthDate;


    private String description;
    private Long nominal;
    private Long donated;
    private List<DonationProgressDTO> progress;

    public static SantriDetailDTO mapFrom(Santri obj) {
        SantriDetailDTOBuilder builder = builder()
                .id(obj.getId())
                .name(obj.getName())
                .slug(obj.getSlug())
                .background(obj.getBackground())
                .thumbnailURL(obj.getThumbnailURL())
                .address(obj.getAddress())
                .markaz(MarkazDetailDTO.mapFrom(obj.getMarkaz()))
                .gender(obj.getGender())
                .birthPlace(obj.getBirthPlace())
                .birthDate(obj.getBirthDate());

        if(!obj.getDonationDetails().isEmpty()) {
            DonationDetail donationDetail = obj.getDonationDetails().get(0);
            builder = builder
                    .donationId(donationDetail.getUniqueId())
                    .description(donationDetail.getDescription())
                    .nominal(donationDetail.getNominal())
                    .donated(donationDetail.getDonated())
                    .progress(
                            donationDetail.getProgresses().stream()
                                    .map(DonationProgressDTO::mapFrom)
                                    .collect(Collectors.toList())
                    );
        }

        return builder.build();
    }
}
