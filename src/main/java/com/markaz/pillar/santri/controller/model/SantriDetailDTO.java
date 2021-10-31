package com.markaz.pillar.santri.controller.model;

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

@Data
@Builder
public class SantriDetailDTO {
    @NotNull
    private Integer id;

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
            builder = builder.description(obj.getDonationDetails().get(0).getDescription())
                    .nominal(obj.getDonationDetails().get(0).getNominal())
                    .donated(obj.getDonationDetails().get(0).getDonated());
        }

        return builder.build();
    }
}
