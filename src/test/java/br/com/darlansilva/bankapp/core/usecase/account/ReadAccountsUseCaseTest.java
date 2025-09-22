package br.com.darlansilva.bankapp.core.usecase.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.gateway.AccountGateway;

@ExtendWith(MockitoExtension.class)
class ReadAccountsUseCaseTest {

    private static final EasyRandom RANDOM = new EasyRandom();

    @Mock
    private AccountGateway accountGateway;

    @InjectMocks
    private ReadAccountsUseCase useCase;

    @Nested
    class ReadBy {

        @Test
        void shouldReturnAccounts() {
            var username = RANDOM.nextObject(String.class);
            var accounts = List.of(RANDOM.nextObject(Account.class), RANDOM.nextObject(Account.class));
            given(accountGateway.findBy(username)).willReturn(accounts);

            var result = useCase.readBy(username);

            assertThat(result).isEqualTo(accounts);
            then(accountGateway).should(only()).findBy(username);
        }

        @Test
        void shouldReturnEmptyList() {
            var username = RANDOM.nextObject(String.class);
            given(accountGateway.findBy(username)).willReturn(List.of());

            var result = useCase.readBy(username);

            assertThat(result).isEmpty();
            then(accountGateway).should(only()).findBy(username);
        }

        @Test
        void shouldPropagateException() {
            var username = RANDOM.nextObject(String.class);
            given(accountGateway.findBy(username)).willThrow(new RuntimeException("failure"));

            assertThatThrownBy(() -> useCase.readBy(username))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("failure");

            then(accountGateway).should(only()).findBy(username);
        }
    }
}