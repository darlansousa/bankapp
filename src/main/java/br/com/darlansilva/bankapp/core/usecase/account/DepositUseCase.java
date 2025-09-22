package br.com.darlansilva.bankapp.core.usecase.account;

import static br.com.darlansilva.bankapp.core.domain.TransactionHistoryItem.depositInstance;
import java.math.BigDecimal;
import javax.security.auth.login.AccountNotFoundException;

import org.springframework.stereotype.Component;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.gateway.AccountGateway;
import br.com.darlansilva.bankapp.core.gateway.TransactionHistoryGateway;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DepositUseCase {

    private final AccountGateway accountGateway;
    private final TransactionHistoryGateway transactionHistoryGateway;

    public Account process(Long accountId, String username, BigDecimal amount) throws AccountNotFoundException {
        final var account = accountGateway.findByIdAndUserUsername(accountId, username).orElseThrow(AccountNotFoundException::new);
        account.deposit(amount);
        transactionHistoryGateway.save(account, depositInstance(amount, amount));
        return accountGateway.save(account);
    }
}
