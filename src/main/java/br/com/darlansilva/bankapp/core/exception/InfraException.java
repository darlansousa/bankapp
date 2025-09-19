package br.com.darlansilva.bankapp.core.exception;

public class InfraException extends RuntimeException {

    public InfraException(String code, Throwable cause) {
        super(code, cause);
    }

    public InfraException(String code) {
        super(code);
    }

}
