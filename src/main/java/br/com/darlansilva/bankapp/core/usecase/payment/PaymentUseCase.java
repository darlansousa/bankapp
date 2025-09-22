package br.com.darlansilva.bankapp.core.usecase.payment;

import static br.com.darlansilva.bankapp.core.domain.TransactionHistoryItem.paymentInstance;
import static java.time.LocalDateTime.now;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.domain.AccountType;
import br.com.darlansilva.bankapp.core.exception.PaymentAccountNotFoundException;
import br.com.darlansilva.bankapp.core.gateway.AccountGateway;
import br.com.darlansilva.bankapp.core.gateway.TransactionHistoryGateway;
import br.com.darlansilva.bankapp.core.usecase.payment.dto.PaymentOutputDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentUseCase {

    private final AccountGateway accountGateway;
    private final TransactionHistoryGateway transactionHistoryGateway;

    public PaymentOutputDto processPayment(String documentNumber, BigDecimal amount, String username) {
        Account savedAccount = accountGateway.findBy(username)
                .stream()
                .filter(account -> account.getType().equals(AccountType.CHECKING))
                .findFirst().orElseThrow(PaymentAccountNotFoundException::new);
        savedAccount.pay(amount);
        accountGateway.save(savedAccount);

        transactionHistoryGateway.save(savedAccount, paymentInstance(amount, amount, documentNumber));

        return new PaymentOutputDto(documentNumber,
                                    amount,
                                    savedAccount.getId(),
                                    savedAccount.getBalance(),
                                    now());
    }
}
