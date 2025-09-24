package br.com.darlansilva.bankapp.core.usecase.account;


import static br.com.darlansilva.bankapp.core.domain.TransactionHistoryItem.withdrawalInstance;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.exception.AccountNotFoundException;
import br.com.darlansilva.bankapp.core.gateway.AccountGateway;
import br.com.darlansilva.bankapp.core.gateway.TransactionHistoryGateway;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WithdrawalUseCase {

    private final AccountGateway accountGateway;
    private final TransactionHistoryGateway transactionHistoryGateway;

    @Transactional
    public Account process(Long accountId, String username, BigDecimal amount) {
        final var account = accountGateway.findByIdAndUserUsername(accountId, username)
                 .orElseThrow(AccountNotFoundException::new);
         account.withdrawal(amount);
        accountGateway.save(account);
        transactionHistoryGateway.save(account, withdrawalInstance(amount, amount));
        return account;
    }
}
