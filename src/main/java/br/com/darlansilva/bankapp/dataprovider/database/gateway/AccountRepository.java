package br.com.darlansilva.bankapp.dataprovider.database.gateway;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.gateway.AccountGateway;
import br.com.darlansilva.bankapp.dataprovider.database.entity.AccountEntity;
import br.com.darlansilva.bankapp.dataprovider.database.mapper.AccountEntityMapper;
import br.com.darlansilva.bankapp.dataprovider.database.repository.AccountJPARepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountRepository implements AccountGateway {

    private final AccountJPARepository repository;
    private final AccountEntityMapper mapper;

    @Override
    public Account save(Account account) {
        AccountEntity entity = mapper.toEntity(account);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public List<Account> findBy(String username) {
        return repository.findByUserUsername(username).stream()
                         .map(mapper::toDomain)
                         .toList();
    }

    @Override
    public Optional<Account> findBy(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Account> findByIdAndUsernameWithHistory(Long id, String username) {
        return repository.findByIdAndUserWithHistory(id, username).map(mapper::toDomain);
    }

    @Override
    public Optional<Account> findByIdAndUserUsername(Long id, String username) {
        return repository.findByIdAndUserUsername(id, username).map(mapper::toDomain);
    }
}
