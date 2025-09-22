package br.com.darlansilva.bankapp.dataprovider.database.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.darlansilva.bankapp.dataprovider.database.entity.AuthorityEntity;
import br.com.darlansilva.bankapp.dataprovider.database.entity.HistoryItemEntity;

@Repository
public interface HistoryItemJPARepository extends CrudRepository<HistoryItemEntity, Long> {

}