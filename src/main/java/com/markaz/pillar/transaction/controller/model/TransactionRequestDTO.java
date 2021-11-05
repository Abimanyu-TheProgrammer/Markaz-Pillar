package com.markaz.pillar.transaction.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class TransactionRequestDTO {
    @ToString.Include
    private Integer markaz;

    @ToString.Include
    private Integer santri;

    @NotNull
    private Long amount;
}
