package com.markaz.pillar.volunteer.admin.controller.model;

import com.markaz.pillar.volunteer.repository.model.RegistrationStatus;
import com.markaz.pillar.volunteer.repository.model.VolunteerRegistration;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class RegistrationDTO {
    @NotNull
    private Integer id;

    @NotNull
    private VolunteerProgramSimpleDTO program;

    @NotNull
    private String name;

    @NotNull
    private String ktp;

    @NotNull
    private String phoneNum;

    @NotNull
    private String email;

    @NotNull
    private String address;

    @NotNull
    private String pictureURL;

    @NotNull
    private String essayURL;

    @NotNull
    private String cvURL;

    @NotNull
    private RegistrationStatus status;

    private String reason;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private String updatedBy;

    public static RegistrationDTO mapFrom(VolunteerRegistration obj) {
        return builder()
                .id(obj.getId())
                .program(VolunteerProgramSimpleDTO.mapFrom(obj.getProgram()))
                .name(obj.getName())
                .ktp(obj.getKtp())
                .phoneNum(obj.getPhoneNum())
                .email(obj.getEmail())
                .address(obj.getEmail())
                .pictureURL(obj.getPictureURL())
                .essayURL(obj.getEssayURL())
                .cvURL(obj.getCvURL())
                .status(obj.getStatus())
                .reason(obj.getReason())
                .createdAt(obj.getCreatedAt())
                .updatedAt(obj.getUpdatedAt())
                .updatedBy(obj.getUpdatedBy())
                .build();
    }
}
