package br.com.darlansilva.bankapp.core.exception;

public class NotAuthorizedTransaction extends UseCaseException{
    public NotAuthorizedTransaction(String code) {
        super(code);
    }
}
