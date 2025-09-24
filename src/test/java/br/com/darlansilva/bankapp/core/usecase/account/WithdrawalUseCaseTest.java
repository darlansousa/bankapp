package br.com.darlansilva.bankapp.core.usecase.account;

import static br.com.darlansilva.bankapp.core.domain.TransactionHistoryItem.withdrawalInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.domain.AccountType;
import br.com.darlansilva.bankapp.core.domain.User;
import br.com.darlansilva.bankapp.core.exception.AccountNotFoundException;
import br.com.darlansilva.bankapp.core.gateway.AccountGateway;
import br.com.darlansilva.bankapp.core.gateway.TransactionHistoryGateway;

@ExtendWith(MockitoExtension.class)
class WithdrawalUseCaseTest {

    private static final EasyRandom EASY_RANDOM = new EasyRandom();

    @InjectMocks
    private WithdrawalUseCase subject;

    @Mock
    private AccountGateway accountGatewayMock;

    @Mock
    private TransactionHistoryGateway historyGatewayMock;

    @Test
    void shouldWithdrawAndRegisterHistory() throws Exception {

        final var username = EASY_RANDOM.nextObject(String.class);
        final var accountId = EASY_RANDOM.nextObject(Long.class);

        final var amountRaw = EASY_RANDOM.nextObject(BigDecimal.class);
        final var amount = amountRaw.abs().setScale(2, RoundingMode.DOWN);

        final var extraRaw = EASY_RANDOM.nextObject(BigDecimal.class);
        final var extra = extraRaw.abs().setScale(2, RoundingMode.DOWN);
        final var initialBal = amount.add(extra);

        final var user = EASY_RANDOM.nextObject(User.class);
        final var account = Account.from(AccountType.CHECKING, initialBal, user);

        given(accountGatewayMock.findByIdAndUserUsername(accountId, username))
                .willReturn(Optional.of(account));
        given(accountGatewayMock.save(account)).willReturn(account);

        final var result = subject.process(accountId, username, amount);

        final var expectedBalance = initialBal.subtract(amount).setScale(2, RoundingMode.DOWN);
        assertEquals(expectedBalance, result.getBalance());
        assertEquals(account, result);

        then(accountGatewayMock).should().findByIdAndUserUsername(accountId, username);
        then(accountGatewayMock).should().save(account);
        then(historyGatewayMock).should().save(account, withdrawalInstance(amount, amount));
    }

    @Test
    void shouldThrowWhenAccountNotFound() {

        final var username = EASY_RANDOM.nextObject(String.class);
        final var accountId = EASY_RANDOM.nextObject(Long.class);
        final var amountRaw = EASY_RANDOM.nextObject(BigDecimal.class);
        final var amount = amountRaw.abs().setScale(2, RoundingMode.DOWN);

        given(accountGatewayMock.findByIdAndUserUsername(accountId, username)).willReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> subject.process(accountId, username, amount));

        then(accountGatewayMock).should().findByIdAndUserUsername(accountId, username);
        then(accountGatewayMock).should(never()).save(any());
        then(historyGatewayMock).should(never()).save(any(), any());
    }
}