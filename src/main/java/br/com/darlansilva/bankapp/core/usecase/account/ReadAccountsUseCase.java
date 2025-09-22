package br.com.darlansilva.bankapp.core.usecase.account;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.gateway.AccountGateway;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReadAccountsUseCase {

    private final AccountGateway accountGateway;

    public List<Account> readBy(String username) {
        return accountGateway.findBy(
                username
        );
    }


}
