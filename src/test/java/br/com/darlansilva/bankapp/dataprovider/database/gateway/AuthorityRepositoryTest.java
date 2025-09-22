package br.com.darlansilva.bankapp.dataprovider.database.gateway;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.darlansilva.bankapp.core.domain.Authority;
import br.com.darlansilva.bankapp.dataprovider.database.entity.AuthorityEntity;
import br.com.darlansilva.bankapp.dataprovider.database.entity.UserEntity;
import br.com.darlansilva.bankapp.dataprovider.database.mapper.AuthorityEntityMapper;
import br.com.darlansilva.bankapp.dataprovider.database.repository.AuthorityJPARepository;


@ExtendWith(MockitoExtension.class)
class AuthorityRepositoryTest {

    private static final EasyRandom EASY_RANDOM = new EasyRandom();

    @InjectMocks
    private AuthorityRepository subject;

    @Mock
    private AuthorityJPARepository repositoryMock;

    @Mock
    private AuthorityEntityMapper mapperMock;

    @Test
    void shouldRegisterAuthority() {
        final var domain = EASY_RANDOM.nextObject(Authority.class);
        final var entity = AuthorityEntity.builder()
                .id(domain.getId())
                .role(domain.getRole())
                .user(UserEntity.builder()
                              .username(domain.getUser().getUsername())
                              .password(domain.getUser().getPassword())
                              .build())
                .build();

        given(repositoryMock.save(entity)).willReturn(entity);
        given(mapperMock.toEntity(domain)).willReturn(entity);

        subject.register(domain);

        then(repositoryMock).should().save(entity);
        then(mapperMock).should().toEntity(domain);
    }

}