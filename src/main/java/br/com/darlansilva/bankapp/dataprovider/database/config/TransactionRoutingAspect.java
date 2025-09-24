package br.com.darlansilva.bankapp.dataprovider.database.config;

import static br.com.darlansilva.bankapp.dataprovider.database.config.DatabaseSourceContext.READ;
import static br.com.darlansilva.bankapp.dataprovider.database.config.DatabaseSourceContext.WRITE;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
public class TransactionRoutingAspect {

    @Before("@annotation(transactional)")
    public void beforeTransactional(Transactional transactional) {
        if (transactional.readOnly()) {
            TransactionRoutingContext.setDataSourceType(READ);
        } else {
            TransactionRoutingContext.setDataSourceType(WRITE);
        }
    }

    @After("@annotation(transactional)")
    public void afterTransactional(Transactional transactional) {
        TransactionRoutingContext.clear();
    }
}
