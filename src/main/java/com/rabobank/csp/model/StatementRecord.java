package com.rabobank.csp.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
public class StatementRecord {
    private @Id Long transactionReference;
    private @NonNull @Column(length=34) String accountNumber;
    private @Column(precision = 19, scale = 4) BigDecimal startBalance;
    private @Column(precision = 19, scale = 4) BigDecimal mutation;
    private String description;
    private @Column(precision = 19, scale = 4) BigDecimal endBalance;
}
