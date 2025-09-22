package br.com.darlansilva.bankapp.core.usecase.account;

import static java.math.BigDecimal.ZERO;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.domain.AccountType;
import br.com.darlansilva.bankapp.core.domain.TransactionHistoryItem;
import br.com.darlansilva.bankapp.core.domain.User;
import br.com.darlansilva.bankapp.core.exception.AccountAlreadyExistisException;
import br.com.darlansilva.bankapp.core.exception.UserNotFoundException;
import br.com.darlansilva.bankapp.core.gateway.AccountGateway;
import br.com.darlansilva.bankapp.core.gateway.TransactionHistoryGateway;
import br.com.darlansilva.bankapp.core.gateway.UserGateway;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveAccountUseCase {

    private final AccountGateway accountGateway;
    private final UserGateway userGateway;
    private final TransactionHistoryGateway transactionHistoryGateway;

    public Account createAccount(AccountType type, BigDecimal initialBalance, String username) {
        if(accountGateway.findBy(username).isEmpty()) {
            User user = userGateway.findBy(username).orElseThrow(UserNotFoundException::new);
            Account saved = accountGateway.save(Account.from(type, initialBalance, user));
            if(initialBalance.compareTo(ZERO) > 0) {
                transactionHistoryGateway.save(saved, TransactionHistoryItem.init(initialBalance));
            }
            return saved;
        }
        throw new AccountAlreadyExistisException() ;
    }
}
