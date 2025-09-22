package br.com.darlansilva.bankapp.dataprovider.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.darlansilva.bankapp.dataprovider.database.entity.AccountEntity;

@Repository
public interface AccountJPARepository extends CrudRepository<AccountEntity, Long> {

    List<AccountEntity> findByUserUsername(String username);
    @EntityGraph(attributePaths = "history")
    @Query("select a from AccountEntity a where a.id = :id and a.user.username = :username")
    Optional<AccountEntity> findByIdAndUserWithHistory(@Param("id") Long id,@Param("username") String username);
    Optional<AccountEntity> findByIdAndUserUsername(Long id,String username);
}