package br.com.darlansilva.bankapp.dataprovider.database.gateway;

import org.springframework.stereotype.Component;

import br.com.darlansilva.bankapp.core.domain.Authority;
import br.com.darlansilva.bankapp.core.gateway.AuthorityGateway;
import br.com.darlansilva.bankapp.dataprovider.database.mapper.AuthorityEntityMapper;
import br.com.darlansilva.bankapp.dataprovider.database.repository.AuthorityJPARepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthorityRepository implements AuthorityGateway {

    private final AuthorityJPARepository repository;
    private final AuthorityEntityMapper mapper;

    @Override
    public void register(Authority authority) {
        repository.save(mapper.toEntity(authority));
    }
}
