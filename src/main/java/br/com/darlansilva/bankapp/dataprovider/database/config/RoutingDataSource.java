package br.com.darlansilva.bankapp.dataprovider.database.config;

import static br.com.darlansilva.bankapp.dataprovider.database.config.DatabaseSourceContext.READ;
import static br.com.darlansilva.bankapp.dataprovider.database.config.DatabaseSourceContext.WRITE;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        return readOnly ? READ: WRITE;
    }
}
