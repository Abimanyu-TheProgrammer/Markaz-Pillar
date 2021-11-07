package com.markaz.pillar.transaction.controller.model;

import com.markaz.pillar.donation.repository.model.DonationDetail;
import com.markaz.pillar.transaction.repository.model.TransactionStatus;
import com.markaz.pillar.transaction.repository.model.UserTransaction;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class TransactionDTO {
    @NotNull
    @ToString.Include
    private String trxId;

    @NotNull
    private String paymentURL;

    @NotNull
    private Long amount;

    @NotNull
    private Integer donationId;

    @NotNull
    private String donationUniqueId;

    @NotNull
    private String donationName;

    @NotNull
    private String donationType;

    @NotNull
    private String userEmail;

    @NotNull
    private TransactionStatus status;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private String updatedBy;
    private String reason;

    public static TransactionDTO mapFrom(UserTransaction obj) {
        DonationDetail donationDetail = obj.getDonationDetail();
        return builder()
                .trxId(obj.getTrxId())
                .paymentURL(obj.getDonationURL())
                .amount(obj.getAmount())
                .donationId(donationDetail.getId())
                .donationUniqueId(donationDetail.getUniqueId())
                .donationName(
                        donationDetail.getMarkaz() == null ?
                                donationDetail.getSantri().getName() : donationDetail.getMarkaz().getName()
                )
                .donationType(donationDetail.getMarkaz() == null ? "SANTRI" : "MARKAZ")
                .userEmail(obj.getUser().getEmail())
                .status(obj.getStatus())
                .createdAt(obj.getCreatedAt())
                .updatedAt(obj.getUpdatedAt())
                .updatedBy(obj.getUpdatedBy())
                .reason(obj.getReason())
                .build();
    }
}
