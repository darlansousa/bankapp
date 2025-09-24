package br.com.darlansilva.bankapp.dataprovider.database.gateway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.domain.TransactionHistoryItem;
import br.com.darlansilva.bankapp.dataprovider.database.entity.AccountEntity;
import br.com.darlansilva.bankapp.dataprovider.database.entity.HistoryItemEntity;
import br.com.darlansilva.bankapp.dataprovider.database.mapper.AccountEntityMapper;
import br.com.darlansilva.bankapp.dataprovider.database.repository.HistoryItemJPARepository;

@ExtendWith(MockitoExtension.class)
class TransactionHistoryRepositoryTest {

    private static final EasyRandom EASY_RANDOM = new EasyRandom();

    @InjectMocks
    private TransactionHistoryRepository subject;

    @Mock
    private HistoryItemJPARepository repositoryMock;

    @Mock
    private AccountEntityMapper mapperMock;

    @Captor
    private ArgumentCaptor<HistoryItemEntity> entityCaptor;

    @Test
    void shouldSaveHistoryItemWithMappedAccountAndValues() {
        final var account = EASY_RANDOM.nextObject(Account.class);
        final var item = EASY_RANDOM.nextObject(TransactionHistoryItem.class);

        final var amount = new BigDecimal("123.45");
        final var before = new BigDecimal("1000.00");
        final var after  = new BigDecimal("876.55");

        final var deterministicItem = new TransactionHistoryItem(
                null,
                item.getType(),
                amount,
                before,
                after,
                item.getReferenceId(),
                item.getCreated());

        final var mappedAccountEntity = EASY_RANDOM.nextObject(AccountEntity.class);

        given(mapperMock.toEntity(account)).willReturn(mappedAccountEntity);

        subject.save(account, deterministicItem);

        then(mapperMock).should().toEntity(account);
        then(repositoryMock).should().save(entityCaptor.capture());

        final var saved = entityCaptor.getValue();
        assertThat(saved.getAccount()).isSameAs(mappedAccountEntity);
        assertThat(saved.getType()).isEqualTo(deterministicItem.getType());
        assertThat(saved.getReferenceId()).isEqualTo(deterministicItem.getReferenceId());

        assertThat(saved.getAmount().compareTo(amount)).isZero();
        assertThat(saved.getBalanceBefore().compareTo(before)).isZero();
        assertThat(saved.getBalanceAfter().compareTo(after)).isZero();
    }

    @Test
    void shouldHandleNullAccountMappingGracefully() {

        final var account = EASY_RANDOM.nextObject(Account.class);
        final var item = EASY_RANDOM.nextObject(TransactionHistoryItem.class);

        given(mapperMock.toEntity(account)).willReturn(null);

        subject.save(account, item);

        then(mapperMock).should().toEntity(account);
        then(repositoryMock).should().save(entityCaptor.capture());

        final var saved = entityCaptor.getValue();
        assertThat(saved.getAccount()).isNull();
        assertThat(saved.getType()).isEqualTo(item.getType());
        assertThat(saved.getReferenceId()).isEqualTo(item.getReferenceId());
        if (item.getAmount() != null) {
            assertThat(saved.getAmount().compareTo(item.getAmount())).isZero();
        } else {
            assertThat(saved.getAmount()).isNull();
        }
    }
}