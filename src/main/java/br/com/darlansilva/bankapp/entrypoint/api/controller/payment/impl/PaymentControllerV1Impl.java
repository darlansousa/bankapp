package br.com.darlansilva.bankapp.entrypoint.api.controller.payment.impl;

import java.security.Principal;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.darlansilva.bankapp.core.usecase.payment.PaymentUseCase;
import br.com.darlansilva.bankapp.entrypoint.api.controller.payment.PaymentController;
import br.com.darlansilva.bankapp.entrypoint.api.dto.input.PaymentInputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.PaymentTransactionOutputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("v1/payments")
@Tag(name = "Pagamentos", description = "Através deste recurso é possível fazer pagamentos")
public class PaymentControllerV1Impl implements PaymentController {

    private final PaymentUseCase paymentUseCase;


    @PostMapping
    @Operation(
            summary = "Fazer pagamento",
            description = "Fazer uma transação de pagamento",
            responses = {
                    @ApiResponse(responseCode = "201", content = {
                            @Content(schema = @Schema(implementation = PaymentTransactionOutputDto.class))
                    }),
                    @ApiResponse(responseCode = "422", description = "Erro na transação")
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(cacheNames="balance", allEntries = true)
    @Override
    public PaymentTransactionOutputDto pay(@RequestBody @Valid PaymentInputDto input, Principal principal) {
        return paymentUseCase.processPayment(
                input.getDocumentNumber(),
                input.getAmount(),
                principal.getName()
        ).toOutputTransaction();
    }
}
