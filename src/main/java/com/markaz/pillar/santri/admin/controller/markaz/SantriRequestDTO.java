package com.markaz.pillar.santri.admin.controller.markaz;

import com.markaz.pillar.santri.repository.model.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SantriRequestDTO {
    @NotBlank
    @Size(max = 256)
    private String name;

    @NotBlank
    private String background;

    @NotBlank
    @Size(max = 2048)
    private String address;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotBlank
    @Size(max = 128)
    private String birthPlace;

    @NotNull
    private LocalDate birthDate;
}
