package com.markaz.pillar.transaction.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class TransactionRequestDTO {
    @NotNull
    @ToString.Include
    private Integer id;

    @NotNull
    private Long amount;
}
