package br.com.darlansilva.bankapp.core.gateway;

import java.util.List;
import java.util.Optional;

import br.com.darlansilva.bankapp.core.domain.Account;

public interface AccountGateway {

    Account save(Account account);

    List<Account> findBy(String username);

    Optional<Account> findBy(Long id);

    Optional<Account> findByIdAndUsernameWithHistory(Long id, String username);

    Optional<Account> findByIdAndUserUsername(Long id, String username);
}
