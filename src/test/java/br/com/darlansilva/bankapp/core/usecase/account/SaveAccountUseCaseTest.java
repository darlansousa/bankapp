package br.com.darlansilva.bankapp.core.usecase.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.domain.AccountType;
import br.com.darlansilva.bankapp.core.domain.TransactionHistoryItem;
import br.com.darlansilva.bankapp.core.domain.User;
import br.com.darlansilva.bankapp.core.exception.AccountAlreadyExistisException;
import br.com.darlansilva.bankapp.core.exception.UserNotFoundException;
import br.com.darlansilva.bankapp.core.gateway.AccountGateway;
import br.com.darlansilva.bankapp.core.gateway.TransactionHistoryGateway;
import br.com.darlansilva.bankapp.core.gateway.UserGateway;

@ExtendWith(MockitoExtension.class)
class SaveAccountUseCaseTest {

    private static final EasyRandom EASY_RANDOM = new EasyRandom();

    @InjectMocks
    private SaveAccountUseCase subject;

    @Mock
    private AccountGateway accountGatewayMock;

    @Mock
    private UserGateway userGatewayMock;

    @Mock
    private TransactionHistoryGateway historyGatewayMock;

    @Test
    void shouldCreateAccountWithPositiveInitialBalanceAndRegisterHistory() {
        final var username = EASY_RANDOM.nextObject(String.class);
        final var type = AccountType.CHECKING;
        final var initialBalance = EASY_RANDOM.nextObject(BigDecimal.class).abs().setScale(2, RoundingMode.DOWN);
        final var user = EASY_RANDOM.nextObject(User.class);
        final var saved = Account.from(type, initialBalance, user);

        given(accountGatewayMock.findBy(username)).willReturn(List.of());
        given(userGatewayMock.findBy(username)).willReturn(Optional.of(user));
        given(accountGatewayMock.save(any(Account.class))).willReturn(saved);

        final var result = subject.createAccount(type, initialBalance, username);

        assertEquals(saved, result);
        then(accountGatewayMock).should().findBy(username);
        then(userGatewayMock).should().findBy(username);
        then(accountGatewayMock).should().save(any(Account.class));
        then(historyGatewayMock).should().save(saved, TransactionHistoryItem.init(initialBalance));
    }

    @Test
    void shouldCreateAccountWithZeroInitialBalanceWithoutHistory() {
        final var username = EASY_RANDOM.nextObject(String.class);
        final var type = AccountType.SAVINGS;
        final var zero = EASY_RANDOM.nextObject(BigDecimal.class);
        final var initialBalance = zero.subtract(zero).setScale(2, RoundingMode.DOWN);
        final var user = EASY_RANDOM.nextObject(User.class);
        final var saved = Account.from(type, initialBalance, user);

        given(accountGatewayMock.findBy(username)).willReturn(List.of());
        given(userGatewayMock.findBy(username)).willReturn(Optional.of(user));
        given(accountGatewayMock.save(any(Account.class))).willReturn(saved);

        final var result = subject.createAccount(type, initialBalance, username);

        assertEquals(saved, result);
        then(accountGatewayMock).should().findBy(username);
        then(userGatewayMock).should().findBy(username);
        then(accountGatewayMock).should().save(any(Account.class));
        then(historyGatewayMock).should(never()).save(any(), any());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        final var username = EASY_RANDOM.nextObject(String.class);
        final var type = AccountType.CHECKING;
        final var initialBalance = EASY_RANDOM.nextObject(BigDecimal.class).abs().setScale(2, RoundingMode.DOWN);

        given(accountGatewayMock.findBy(username)).willReturn(List.of());
        given(userGatewayMock.findBy(username)).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> subject.createAccount(type, initialBalance, username));

        then(accountGatewayMock).should().findBy(username);
        then(userGatewayMock).should().findBy(username);
        then(accountGatewayMock).should(never()).save(any());
        then(historyGatewayMock).should(never()).save(any(), any());
    }

    @Test
    void shouldThrowWhenAccountAlreadyExists() {
        final var username = EASY_RANDOM.nextObject(String.class);
        final var type = AccountType.CHECKING;
        final var initialBalance = EASY_RANDOM.nextObject(BigDecimal.class).abs().setScale(2, RoundingMode.DOWN);
        final var existing = Account.from(type, initialBalance, EASY_RANDOM.nextObject(User.class));

        given(accountGatewayMock.findBy(username)).willReturn(List.of(existing));

        assertThrows(AccountAlreadyExistisException.class, () -> subject.createAccount(type, initialBalance, username));

        then(accountGatewayMock).should().findBy(username);
        then(userGatewayMock).should(never()).findBy(org.mockito.Mockito.anyString());
        then(accountGatewayMock).should(never()).save(any());
        then(historyGatewayMock).should(never()).save(any(), any());
    }
}