package br.com.darlansilva.bankapp.dataprovider.database.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import br.com.darlansilva.bankapp.core.common.BaseMapper;
import br.com.darlansilva.bankapp.core.domain.TransactionHistoryItem;
import br.com.darlansilva.bankapp.dataprovider.database.entity.HistoryItemEntity;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HistoryItemEntityMapper extends BaseMapper<HistoryItemEntity, TransactionHistoryItem> {
}
