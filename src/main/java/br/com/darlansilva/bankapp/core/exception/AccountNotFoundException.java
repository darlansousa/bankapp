package br.com.darlansilva.bankapp.core.exception;

public class AccountNotFoundException extends UseCaseException{

    public AccountNotFoundException() {
        super("ACCOUNT_NOT_FOUND");
    }
}
