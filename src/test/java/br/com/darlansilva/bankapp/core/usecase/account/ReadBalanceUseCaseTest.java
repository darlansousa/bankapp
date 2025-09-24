package br.com.darlansilva.bankapp.core.usecase.account;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import java.util.Optional;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.exception.AccountNotFoundException;
import br.com.darlansilva.bankapp.core.gateway.AccountGateway;

@ExtendWith(MockitoExtension.class)
class ReadBalanceUseCaseTest {

    private static final EasyRandom RANDOM = new EasyRandom();

    @Mock
    private AccountGateway accountGateway;

    @InjectMocks
    private ReadBalanceUseCase useCase;

    @Nested
    class ReadBalanceWithHistory {

        @Test
        void shouldReturnAccountWhenFound() throws Exception {
            var accountId = RANDOM.nextLong();
            var username = RANDOM.nextObject(String.class);
            var account = RANDOM.nextObject(Account.class);
            given(accountGateway.findByIdAndUsernameWithHistory(accountId, username)).willReturn(Optional.of(account));

            var result = useCase.readBalanceWithHistory(accountId, username);

            assertEquals(result, account);
            then(accountGateway).should(only()).findByIdAndUsernameWithHistory(accountId, username);
        }

        @Test
        void shouldThrowWhenNotFound() {
            var accountId = RANDOM.nextLong();
            var username = RANDOM.nextObject(String.class);
            given(accountGateway.findByIdAndUsernameWithHistory(accountId, username)).willReturn(Optional.empty());
            assertThatThrownBy(() -> useCase.readBalanceWithHistory(accountId, username))
                    .isInstanceOf(AccountNotFoundException.class);

            then(accountGateway).should(only()).findByIdAndUsernameWithHistory(accountId, username);
        }
    }
}