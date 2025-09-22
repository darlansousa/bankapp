package br.com.darlansilva.bankapp.core.usecase.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.darlansilva.bankapp.entrypoint.api.dto.output.PaymentTransactionOutputDto;

public record PaymentOutputDto(
        String documentNumber,
        BigDecimal amount,
        Long accountNumber,
        BigDecimal remainingBalance,
        LocalDateTime transactionDateTime) {

    public PaymentTransactionOutputDto toOutputTransaction() {
        return PaymentTransactionOutputDto.builder()
                .documentNumber(this.documentNumber)
                .amount(this.amount)
                .accountNumber(this.accountNumber)
                .remainingBalance(this.remainingBalance)
                .transactionDateTime(this.transactionDateTime)
                .build();
    }
}
