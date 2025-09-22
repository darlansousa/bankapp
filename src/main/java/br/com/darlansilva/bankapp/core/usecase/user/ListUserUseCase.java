package br.com.darlansilva.bankapp.core.usecase.user;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.darlansilva.bankapp.core.domain.User;
import br.com.darlansilva.bankapp.core.gateway.UserGateway;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ListUserUseCase {
    
    private final UserGateway usersGateway;

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return usersGateway.findAll();
    }

}
