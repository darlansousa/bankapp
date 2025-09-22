package br.com.darlansilva.bankapp.entrypoint.api.dto.input;


import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInputDto {
    @NotNull
    private String documentNumber;
    @Positive
    private BigDecimal amount;
}
