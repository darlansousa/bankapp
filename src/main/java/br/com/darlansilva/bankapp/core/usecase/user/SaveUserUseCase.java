package br.com.darlansilva.bankapp.core.usecase.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.darlansilva.bankapp.core.domain.Authority;
import br.com.darlansilva.bankapp.core.domain.User;
import br.com.darlansilva.bankapp.core.gateway.AuthorityGateway;
import br.com.darlansilva.bankapp.core.gateway.UserGateway;
import br.com.darlansilva.bankapp.core.usecase.user.dto.UserInputDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveUserUseCase {

    private final UserGateway usersGateway;
    private final AuthorityGateway authorityGateway;
    private final PasswordEncoder encoder;

    @Transactional
    public User save(UserInputDto input) {
        final var user = usersGateway.save(input.toDomainWithPassword(encoder.encode(input.password())));
        input.roles().forEach(role -> authorityGateway.register(Authority.from(user, role)));
        return new User(user.getId(), user.getUsername(), user.getPassword(), input.cpf(), input.roles());
    }
}
