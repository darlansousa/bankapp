package br.com.darlansilva.bankapp.entrypoint.api.controller.payment;

import java.security.Principal;

import br.com.darlansilva.bankapp.entrypoint.api.dto.input.PaymentInputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.PaymentTransactionOutputDto;

public interface PaymentController {

    PaymentTransactionOutputDto pay(PaymentInputDto paymentInputDto, Principal principal);

}
