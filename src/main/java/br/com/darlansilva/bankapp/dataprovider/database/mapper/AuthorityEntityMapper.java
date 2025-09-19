package br.com.darlansilva.bankapp.dataprovider.database.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import br.com.darlansilva.bankapp.core.common.BaseMapper;
import br.com.darlansilva.bankapp.core.domain.Authority;
import br.com.darlansilva.bankapp.dataprovider.database.entity.AuthorityEntity;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        uses = {UserEntityMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AuthorityEntityMapper extends BaseMapper<AuthorityEntity, Authority> {
}
