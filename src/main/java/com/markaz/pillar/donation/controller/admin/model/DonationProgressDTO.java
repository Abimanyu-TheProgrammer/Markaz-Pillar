package com.markaz.pillar.donation.controller.admin.model;

import com.markaz.pillar.donation.repository.model.DonationProgress;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class DonationProgressDTO {
    @NotNull
    private Integer id;

    @NotNull
    private String thumbnailURL;

    @NotNull
    private LocalDate progressDate;

    @NotNull
    private String description;

    public static DonationProgressDTO mapFrom(DonationProgress obj) {
        return builder()
                .id(obj.getId())
                .progressDate(obj.getProgressDate())
                .thumbnailURL(obj.getThumbnailURL())
                .description(obj.getDescription())
                .build();
    }
}
