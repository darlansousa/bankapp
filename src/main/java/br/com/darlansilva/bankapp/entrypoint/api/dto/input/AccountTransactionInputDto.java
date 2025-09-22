package br.com.darlansilva.bankapp.entrypoint.api.dto.input;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AccountTransactionInputDto(@NotNull @Positive BigDecimal amount) {
}
