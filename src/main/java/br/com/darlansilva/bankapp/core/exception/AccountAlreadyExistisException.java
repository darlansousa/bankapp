package br.com.darlansilva.bankapp.core.exception;

public class AccountAlreadyExistisException extends UseCaseException{

    public AccountAlreadyExistisException() {
        super("ACCOUNT_ALREADY_EXISTS");
    }
}
