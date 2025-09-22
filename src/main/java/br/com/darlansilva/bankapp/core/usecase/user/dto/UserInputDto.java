package br.com.darlansilva.bankapp.core.usecase.user.dto;

import java.util.List;

import br.com.darlansilva.bankapp.core.domain.User;
import br.com.darlansilva.bankapp.core.domain.UserRole;

public record UserInputDto(
        String username,
        String password,
        String cpf,
        List<UserRole>roles
) {

    public User toDomainWithPassword(String encodedPassword) {
        return User.from(
                this.username,
                this.cpf,
                encodedPassword,
                this.roles
        );
    }
}