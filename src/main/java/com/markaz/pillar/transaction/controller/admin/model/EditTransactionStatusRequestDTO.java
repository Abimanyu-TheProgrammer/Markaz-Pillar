package com.markaz.pillar.transaction.controller.admin.model;

import com.markaz.pillar.transaction.repository.model.TransactionStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class EditTransactionStatusRequestDTO {
    private Long amount;

    @NotNull
    private TransactionStatus status;

    private String reason;
}
