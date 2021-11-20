package com.markaz.pillar.volunteer.admin.controller.model;

import com.markaz.pillar.volunteer.repository.model.RegistrationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class EditRegistrationStatusRequestDTO {
    @NotNull
    private RegistrationStatus status;

    private String reason;
}
