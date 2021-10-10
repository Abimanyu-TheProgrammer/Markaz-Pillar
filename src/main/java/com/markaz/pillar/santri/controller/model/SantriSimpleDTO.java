package com.markaz.pillar.santri.controller.model;

import com.markaz.pillar.markaz.controller.model.MarkazSimpleDTO;
import com.markaz.pillar.santri.repository.model.Gender;
import com.markaz.pillar.santri.repository.model.Santri;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class SantriSimpleDTO {
    @NotNull
    private Integer id;

    @NotNull
    private String slug;

    @NotNull
    private String name;

    @NotNull
    private String thumbnailURL;

    @NotNull
    private String background;

    @NotNull
    private String address;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    private String birthPlace;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private MarkazSimpleDTO markaz;

    public static SantriSimpleDTO mapFrom(Santri obj) {
        return builder()
                .id(obj.getId())
                .slug(obj.getSlug())
                .name(obj.getName())
                .background(obj.getBackground())
                .address(obj.getAddress())
                .gender(obj.getGender())
                .birthPlace(obj.getBirthPlace())
                .birthDate(obj.getBirthDate())
                .thumbnailURL(obj.getThumbnailURL())
                .markaz(MarkazSimpleDTO.mapFrom(obj.getMarkaz()))
                .build();
    }
}
