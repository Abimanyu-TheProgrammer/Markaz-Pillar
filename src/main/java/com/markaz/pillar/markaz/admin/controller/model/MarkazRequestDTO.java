package com.markaz.pillar.markaz.admin.controller.model;

import com.markaz.pillar.markaz.repository.model.MarkazCategory;
import com.markaz.pillar.markaz.repository.model.constraint.EmailOrPhone;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class MarkazRequestDTO {
    @NotBlank
    @Size(max = 256)
    private String name;

    @NotBlank
    private String background;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MarkazCategory category;

    @NotBlank
    @Size(max = 2048)
    private String address;

    @NotBlank
    @Size(max = 512)
    private String contactName;

    @NotBlank
    @Size(max = 512)
    @EmailOrPhone
    private String contactInfo;
}
