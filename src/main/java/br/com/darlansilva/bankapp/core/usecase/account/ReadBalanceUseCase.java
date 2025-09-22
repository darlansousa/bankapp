package br.com.darlansilva.bankapp.core.usecase.account;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.stereotype.Component;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.gateway.AccountGateway;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReadBalanceUseCase {

    private final AccountGateway accountGateway;

    public Account readBalanceWithHistory(Long accountId, String username) throws AccountNotFoundException {
        return accountGateway.findByIdAndUsernameWithHistory(
                accountId,
                username
        ).orElseThrow(AccountNotFoundException::new);
    }


}
