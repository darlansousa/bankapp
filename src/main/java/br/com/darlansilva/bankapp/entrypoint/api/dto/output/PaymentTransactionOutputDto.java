package br.com.darlansilva.bankapp.entrypoint.api.dto.output;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransactionOutputDto {
    private String documentNumber;
    private BigDecimal amount;
    private Long accountNumber;
    private BigDecimal remainingBalance;
    private LocalDateTime transactionDateTime;
}
