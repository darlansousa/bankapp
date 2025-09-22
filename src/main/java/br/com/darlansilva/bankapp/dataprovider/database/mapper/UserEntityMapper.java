package br.com.darlansilva.bankapp.dataprovider.database.mapper;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import br.com.darlansilva.bankapp.core.common.BaseMapper;
import br.com.darlansilva.bankapp.core.domain.User;
import br.com.darlansilva.bankapp.core.domain.UserRole;
import br.com.darlansilva.bankapp.dataprovider.database.entity.AuthorityEntity;
import br.com.darlansilva.bankapp.dataprovider.database.entity.UserEntity;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {AuthorityEntityMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserEntityMapper extends BaseMapper<UserEntity, User> {

    @Override
    @Mapping(target = "roles", qualifiedByName = "mapAuthorities", source = "authorities")
    User toDomain(UserEntity entity);
    @Override

    @Mapping(target = "authorities", ignore = true)
    UserEntity toEntity(User domain);

    @Named("mapAuthorities")
    default List<UserRole> mapAuthorities(List<AuthorityEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream().map(AuthorityEntity::getRole).toList();
    }

}
