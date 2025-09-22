package br.com.darlansilva.bankapp.dataprovider.database.mapper;

import org.springframework.stereotype.Component;

import br.com.darlansilva.bankapp.dataprovider.database.entity.AccountEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class AccountIdMapper {

    @PersistenceContext
    private EntityManager em;

    public AccountEntity fromId(Long id) {
        return (id == null) ? null : em.getReference(AccountEntity.class, id);
    }
}
