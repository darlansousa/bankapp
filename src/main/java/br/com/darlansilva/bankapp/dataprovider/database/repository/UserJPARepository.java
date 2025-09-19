package br.com.darlansilva.bankapp.dataprovider.database.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.darlansilva.bankapp.dataprovider.database.entity.UserEntity;


@Repository
public interface UserJPARepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

}