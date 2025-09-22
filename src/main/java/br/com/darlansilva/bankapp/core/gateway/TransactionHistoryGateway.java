package br.com.darlansilva.bankapp.core.gateway;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.domain.TransactionHistoryItem;

public interface TransactionHistoryGateway {

    void save(Account account, TransactionHistoryItem item);

}
