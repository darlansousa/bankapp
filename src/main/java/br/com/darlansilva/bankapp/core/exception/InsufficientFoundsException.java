package br.com.darlansilva.bankapp.core.exception;


import static br.com.darlansilva.bankapp.core.domain.TransactionStatus.INSUFFICIENT_FUNDS;

public class InsufficientFoundsException extends NotAuthorizedTransaction{
    public InsufficientFoundsException() {
        super(INSUFFICIENT_FUNDS.name());
    }
}
