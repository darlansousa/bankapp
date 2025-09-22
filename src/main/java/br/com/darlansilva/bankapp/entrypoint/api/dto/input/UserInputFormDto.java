package br.com.darlansilva.bankapp.entrypoint.api.dto.input;

import java.util.List;

import br.com.darlansilva.bankapp.core.domain.UserRole;
import br.com.darlansilva.bankapp.entrypoint.api.common.validation.CPF;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInputFormDto {

    @NotNull
    private String username;
    @NotNull
    private String password;
    @CPF
    private String cpf;
    @NotNull
    private List<UserRole> roles;
}
