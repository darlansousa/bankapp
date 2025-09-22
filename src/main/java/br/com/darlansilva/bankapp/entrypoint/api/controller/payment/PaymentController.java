package br.com.darlansilva.bankapp.entrypoint.api.controller.payment;

import br.com.darlansilva.bankapp.entrypoint.api.dto.input.PaymentInputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.PaymentTransactionOutputDto;

public interface PaymentController {

    PaymentTransactionOutputDto pay(PaymentInputDto paymentInputDto, String username);

}
