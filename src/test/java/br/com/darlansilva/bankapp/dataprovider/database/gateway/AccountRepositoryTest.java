package br.com.darlansilva.bankapp.dataprovider.database.gateway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import java.util.List;
import java.util.Optional;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.dataprovider.database.entity.AccountEntity;
import br.com.darlansilva.bankapp.dataprovider.database.mapper.AccountEntityMapper;
import br.com.darlansilva.bankapp.dataprovider.database.repository.AccountJPARepository;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryTest {

    private static final EasyRandom EASY_RANDOM = new EasyRandom();

    @InjectMocks
    private AccountRepository subject;

    @Mock
    private AccountJPARepository repositoryMock;

    @Mock
    private AccountEntityMapper mapperMock;

    @Test
    void shouldSaveMappingDomainToEntityAndBack() {

        final var domainToSave = EASY_RANDOM.nextObject(Account.class);
        final var entityToPersist = EASY_RANDOM.nextObject(AccountEntity.class);
        final var persistedEntity = EASY_RANDOM.nextObject(AccountEntity.class);
        final var mappedBackDomain = EASY_RANDOM.nextObject(Account.class);

        given(mapperMock.toEntity(domainToSave)).willReturn(entityToPersist);
        given(repositoryMock.save(entityToPersist)).willReturn(persistedEntity);
        given(mapperMock.toDomain(persistedEntity)).willReturn(mappedBackDomain);


        final var result = subject.save(domainToSave);


        then(mapperMock).should().toEntity(domainToSave);
        then(repositoryMock).should().save(entityToPersist);
        then(mapperMock).should().toDomain(persistedEntity);
        assertThat(result).isSameAs(mappedBackDomain);
    }

    @Test
    void shouldFindByUsernameMappingEachItem() {

        final var username = EASY_RANDOM.nextObject(String.class);
        final var e1 = EASY_RANDOM.nextObject(AccountEntity.class);
        final var e2 = EASY_RANDOM.nextObject(AccountEntity.class);
        final var d1 = EASY_RANDOM.nextObject(Account.class);
        final var d2 = EASY_RANDOM.nextObject(Account.class);

        given(repositoryMock.findByUserUsername(username)).willReturn(List.of(e1, e2));
        given(mapperMock.toDomain(e1)).willReturn(d1);
        given(mapperMock.toDomain(e2)).willReturn(d2);


        final var result = subject.findBy(username);


        then(repositoryMock).should().findByUserUsername(username);
        then(mapperMock).should().toDomain(e1);
        then(mapperMock).should().toDomain(e2);

        assertThat(result).containsExactly(d1, d2);
    }

    @Test
    void shouldFindByIdAndUsernameWithHistory_whenPresent() {

        final Long id = EASY_RANDOM.nextLong();
        final var username = EASY_RANDOM.nextObject(String.class);
        final var entity = EASY_RANDOM.nextObject(AccountEntity.class);
        final var domain = EASY_RANDOM.nextObject(Account.class);

        given(repositoryMock.findByIdAndUserWithHistory(id, username)).willReturn(Optional.of(entity));
        given(mapperMock.toDomain(entity)).willReturn(domain);


        final var result = subject.findByIdAndUsernameWithHistory(id, username);


        then(repositoryMock).should().findByIdAndUserWithHistory(id, username);
        then(mapperMock).should().toDomain(entity);
        assertThat(result).contains(domain);
    }

    @Test
    void shouldFindByIdAndUsernameWithHistory_whenEmpty() {

        final Long id = EASY_RANDOM.nextLong();
        final var username = EASY_RANDOM.nextObject(String.class);

        given(repositoryMock.findByIdAndUserWithHistory(id, username)).willReturn(Optional.empty());


        final var result = subject.findByIdAndUsernameWithHistory(id, username);


        then(repositoryMock).should().findByIdAndUserWithHistory(id, username);
        then(mapperMock).should(never()).toDomain(any(AccountEntity.class));
        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindByIdAndUserUsername_whenPresent() {

        final Long id = EASY_RANDOM.nextLong();
        final var username = EASY_RANDOM.nextObject(String.class);
        final var entity = EASY_RANDOM.nextObject(AccountEntity.class);
        final var domain = EASY_RANDOM.nextObject(Account.class);

        given(repositoryMock.findByIdAndUserUsername(id, username)).willReturn(Optional.of(entity));
        given(mapperMock.toDomain(entity)).willReturn(domain);


        final var result = subject.findByIdAndUserUsername(id, username);


        then(repositoryMock).should().findByIdAndUserUsername(id, username);
        then(mapperMock).should().toDomain(entity);
        assertThat(result).contains(domain);
    }

    @Test
    void shouldFindByIdAndUserUsername_whenEmpty() {
        final Long id = EASY_RANDOM.nextLong();
        final var username = EASY_RANDOM.nextObject(String.class);
        given(repositoryMock.findByIdAndUserUsername(id, username)).willReturn(Optional.empty());
        final var result = subject.findByIdAndUserUsername(id, username);
        then(repositoryMock).should().findByIdAndUserUsername(id, username);
        then(mapperMock).should(never()).toDomain(any(AccountEntity.class));
        assertThat(result).isEmpty();
    }
}