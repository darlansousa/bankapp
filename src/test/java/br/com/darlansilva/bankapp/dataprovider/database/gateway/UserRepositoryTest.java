package br.com.darlansilva.bankapp.dataprovider.database.gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import java.util.List;
import java.util.Optional;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.darlansilva.bankapp.core.domain.User;
import br.com.darlansilva.bankapp.dataprovider.database.entity.UserEntity;
import br.com.darlansilva.bankapp.dataprovider.database.mapper.UserEntityMapper;
import br.com.darlansilva.bankapp.dataprovider.database.repository.UserJPARepository;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    private static final EasyRandom EASY_RANDOM = new EasyRandom();

    @InjectMocks
    private UserRepository subject;

    @Mock
    private UserJPARepository repositoryMock;

    @Mock
    private UserEntityMapper mapperMock;


    @Test
    void shouldFindAllUsers() {
        final var domain = EASY_RANDOM.nextObject(User.class);
        final var user = UserEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .password(domain.getPassword())
                .build();
        final var expected = List.of(domain);

        given(repositoryMock.findAll()).willReturn(List.of(user));
        given(mapperMock.toDomain(user)).willReturn(domain);

        assertEquals(expected, subject.findAll());

        then(repositoryMock).should().findAll();
        then(mapperMock).should().toDomain(user);
    }


    @Test
    void shouldFindUserById() {
        final var domain = EASY_RANDOM.nextObject(User.class);
        final var user = UserEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .password(domain.getPassword())
                .build();
        final var expected = Optional.of(domain);

        given(repositoryMock.findById(user.getId())).willReturn(Optional.of(user));
        given(mapperMock.toDomain(user)).willReturn(domain);

        assertEquals(expected, subject.findBy(user.getId()));

        then(repositoryMock).should().findById(user.getId());
        then(mapperMock).should().toDomain(user);
    }

    @Test
    void shouldFindUserByUserName() {
        final var domain = EASY_RANDOM.nextObject(User.class);
        final var user = UserEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .password(domain.getPassword())
                .build();
        final var expected = Optional.of(domain);

        given(repositoryMock.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));
        given(mapperMock.toDomain(user)).willReturn(domain);

        assertEquals(expected, subject.findBy(user.getUsername()));

        then(repositoryMock).should().findByUsername(user.getUsername());
        then(mapperMock).should().toDomain(user);
    }


    @Test
    void shouldReturnsEmptyWhenUserNotExists() {
        final var id = EASY_RANDOM.nextLong();
        final var expected = Optional.empty();

        given(repositoryMock.findById(id)).willReturn(Optional.empty());

        assertEquals(expected, subject.findBy(id));

        then(repositoryMock).should().findById(id);
        then(mapperMock).shouldHaveNoInteractions();
    }

    @Test
    void shouldSaveUser() {
        final var domain = EASY_RANDOM.nextObject(User.class);
        final var user = UserEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .password(domain.getPassword())
                .build();

        given(repositoryMock.save(user)).willReturn(user);
        given(mapperMock.toDomain(user)).willReturn(domain);
        given(mapperMock.toEntity(domain)).willReturn(user);

        assertEquals(domain, subject.save(domain));

        then(repositoryMock).should().save(user);
        then(mapperMock).should().toDomain(user);
        then(mapperMock).should().toEntity(domain);
    }

    @Test
    void shouldDelete() {
        final var id = EASY_RANDOM.nextLong();
        subject.deleteBy(id);
        then(repositoryMock).should().deleteById(id);
        then(mapperMock).shouldHaveNoInteractions();
    }


}