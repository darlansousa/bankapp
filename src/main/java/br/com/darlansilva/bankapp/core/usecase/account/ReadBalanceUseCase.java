package br.com.darlansilva.bankapp.core.usecase.account;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.gateway.AccountGateway;
import br.com.darlansilva.bankapp.core.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReadBalanceUseCase {

    private final AccountGateway accountGateway;

    @Transactional(readOnly = true)
    public Account readBalanceWithHistory(Long accountId, String username)  {
        return accountGateway.findByIdAndUsernameWithHistory(
                accountId,
                username
        ).orElseThrow(AccountNotFoundException::new);
    }
}
