package br.com.darlansilva.bankapp.core.exception;

public class PaymentAccountNotFoundException extends UseCaseException{
    public PaymentAccountNotFoundException() {
        super("PAYMENT_ACCOUNT_NOT_FOUND");
    }
}
