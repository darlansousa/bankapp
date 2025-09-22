package br.com.darlansilva.bankapp.dataprovider.database.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import br.com.darlansilva.bankapp.core.common.BaseMapper;
import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.domain.Authority;
import br.com.darlansilva.bankapp.dataprovider.database.entity.AccountEntity;
import br.com.darlansilva.bankapp.dataprovider.database.entity.AuthorityEntity;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AccountEntityMapper extends BaseMapper<AccountEntity, Account> {
}
