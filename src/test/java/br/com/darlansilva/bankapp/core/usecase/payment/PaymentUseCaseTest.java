package br.com.darlansilva.bankapp.core.usecase.payment;

import static br.com.darlansilva.bankapp.core.domain.AccountType.CHECKING;
import static br.com.darlansilva.bankapp.core.domain.AccountType.SAVINGS;
import static br.com.darlansilva.bankapp.core.domain.TransactionHistoryItem.paymentInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.domain.User;
import br.com.darlansilva.bankapp.core.exception.PaymentAccountNotFoundException;
import br.com.darlansilva.bankapp.core.gateway.AccountGateway;
import br.com.darlansilva.bankapp.core.gateway.TransactionHistoryGateway;
import br.com.darlansilva.bankapp.core.usecase.payment.dto.PaymentOutputDto;

@ExtendWith(MockitoExtension.class)
class PaymentUseCaseTest {

    private static final EasyRandom EASY_RANDOM = new EasyRandom();

    @InjectMocks
    private PaymentUseCase subject;

    @Mock
    private AccountGateway accountGatewayMock;

    @Mock
    private TransactionHistoryGateway historyGatewayMock;

    @Test
    void shouldProcessPaymentUsingCheckingAccountAndRegisterHistory() {
        
        final var username = EASY_RANDOM.nextObject(String.class);
        final var documentNumber = EASY_RANDOM.nextObject(String.class);
        final var amount = BigDecimal.TEN;
        BigDecimal balance = BigDecimal.valueOf(100);

        final var savings = Account.from(SAVINGS, balance, EASY_RANDOM.nextObject(User.class));
        final var checking = Account.from(CHECKING, balance, EASY_RANDOM.nextObject(User.class));

        final var expected = new PaymentOutputDto(documentNumber, amount, checking.getId(),
                                                  BigDecimal.valueOf(90).setScale(2, RoundingMode.DOWN),
                                                  LocalDateTime.now());

        given(accountGatewayMock.findBy(username)).willReturn(List.of(savings, checking));
        given(accountGatewayMock.save(checking)).willReturn(checking);

        final var result = subject.processPayment(documentNumber, amount, username);

        assertEquals(expected.remainingBalance(), result.remainingBalance());
        assertEquals(expected.amount(), result.amount());
        assertEquals(expected.accountNumber(), result.accountNumber());

        then(accountGatewayMock).should().findBy(username);
        then(accountGatewayMock).should().save(checking);
        then(accountGatewayMock).should(never()).save(savings);
        then(historyGatewayMock).should().save(checking, paymentInstance(amount, balance, documentNumber));
    }

    @Test
    void shouldThrowWhenNoCheckingAccountFound() {
        
        final var username = EASY_RANDOM.nextObject(String.class);
        final var documentNumber = EASY_RANDOM.nextObject(String.class);
        final var amount = EASY_RANDOM.nextObject(BigDecimal.class);

        
        final var onlySavings = Account.from(SAVINGS, amount, EASY_RANDOM.nextObject(User.class));

        given(accountGatewayMock.findBy(username)).willReturn(List.of(onlySavings));

        
        assertThrows(PaymentAccountNotFoundException.class,
                     () -> subject.processPayment(documentNumber, amount, username));

        then(accountGatewayMock).should().findBy(username);
        then(accountGatewayMock).should(never()).save(any());
        then(historyGatewayMock).should(never()).save(any(), any());
    }
}