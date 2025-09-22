package br.com.darlansilva.bankapp.entrypoint.api.dto.input;

import java.math.BigDecimal;

import br.com.darlansilva.bankapp.core.domain.AccountType;
import jakarta.validation.constraints.NotNull;


public record AccountInputDto(@NotNull AccountType type, @NotNull BigDecimal initialBalance) {
}
