package br.com.darlansilva.bankapp.dataprovider.database.gateway;

import org.springframework.stereotype.Component;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.domain.TransactionHistoryItem;
import br.com.darlansilva.bankapp.core.gateway.TransactionHistoryGateway;
import br.com.darlansilva.bankapp.dataprovider.database.entity.HistoryItemEntity;
import br.com.darlansilva.bankapp.dataprovider.database.mapper.AccountEntityMapper;
import br.com.darlansilva.bankapp.dataprovider.database.repository.HistoryItemJPARepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionHistoryRepository implements TransactionHistoryGateway {

    private final HistoryItemJPARepository repository;
    private final AccountEntityMapper mapper;
    @Override
    public void save(Account account, TransactionHistoryItem item) {
        repository.save(HistoryItemEntity.builder()
                                .type(item.getType())
                                .account(mapper.toEntity(account))
                                .balanceBefore(item.getBalanceBefore())
                                .balanceAfter(item.getBalanceAfter())
                                .amount(item.getAmount())
                                .referenceId(item.getReferenceId())
                                .build());
    }
}
