package br.com.darlansilva.bankapp.entrypoint.api.dto.output;


import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.darlansilva.bankapp.core.domain.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountOutputDto {
    private Long accountId;
    private AccountType type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal balance;
    private String username;
}
