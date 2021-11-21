package com.markaz.pillar.markaz.controller.model;

import com.markaz.pillar.donation.controller.admin.model.DonationProgressDTO;
import com.markaz.pillar.donation.repository.model.DonationDetail;
import com.markaz.pillar.donation.repository.model.MarkazDonationCategory;
import com.markaz.pillar.markaz.repository.model.Markaz;
import com.markaz.pillar.markaz.repository.model.MarkazCategory;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class MarkazDetailDTO {
    @NotNull
    private Integer id;

    @NotNull
    private String donationId;

    @NotNull
    @ToString.Include
    private String name;

    @NotNull
    private String slug;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MarkazCategory category;

    @NotNull
    private String background;

    @NotNull
    private String thumbnailURL;

    @NotNull
    private String address;

    @NotEmpty
    private Set<MarkazDonationCategory> donationCategories;

    @NotNull
    private String contactName;

    @NotNull
    private String contactInfo;

    private String description;
    private Long nominal;
    private Long donated;
    private List<DonationProgressDTO> progress;

    public static MarkazDetailDTO mapFrom(Markaz obj) {
        MarkazDetailDTOBuilder builder = builder()
                .id(obj.getId())
                .name(obj.getName())
                .slug(obj.getSlug())
                .category(obj.getCategory())
                .background(obj.getBackground())
                .thumbnailURL(obj.getThumbnailURL())
                .address(obj.getAddress())
                .contactName(obj.getContactName())
                .contactInfo(obj.getContactInfo());

        if(!obj.getDonationDetails().isEmpty()) {
            DonationDetail donationDetail = obj.getDonationDetails().get(0);
            builder = builder
                    .donationId(donationDetail.getUniqueId())
                    .donationCategories(donationDetail.getCategories())
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
