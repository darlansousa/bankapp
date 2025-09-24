package br.com.darlansilva.bankapp.entrypoint.api.controller.account.impl;

import static java.time.LocalDateTime.now;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.darlansilva.bankapp.core.exception.AccountNotFoundException;
import br.com.darlansilva.bankapp.core.usecase.account.DepositUseCase;
import br.com.darlansilva.bankapp.core.usecase.account.ReadAccountsUseCase;
import br.com.darlansilva.bankapp.core.usecase.account.ReadBalanceUseCase;
import br.com.darlansilva.bankapp.core.usecase.account.SaveAccountUseCase;
import br.com.darlansilva.bankapp.core.usecase.account.WithdrawalUseCase;
import br.com.darlansilva.bankapp.entrypoint.api.controller.account.AccountController;
import br.com.darlansilva.bankapp.entrypoint.api.dto.input.AccountInputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.input.AccountTransactionInputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.AccountBalanceOutputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.AccountOutputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.AccountTransactionOutputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.HistoryItemDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/accounts")
@Tag(name = "Contas", description = "Através deste recurso é possível gerenciar as contas dos usuários do sistema")
public class AccountControllerV1Impl implements AccountController {

    private final SaveAccountUseCase saveAccountUseCase;
    private final ReadBalanceUseCase readBalanceUseCase;
    private final WithdrawalUseCase withdrawalUseCase;
    private final DepositUseCase depositUseCase;
    private final ReadAccountsUseCase readAccountsUseCase;


    @PostMapping
    @Operation(summary = "Criar conta", description = "Criar uma nova conta para o usuário autenticado", responses = {
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = AccountOutputDto.class))}),
            @ApiResponse(responseCode = "422", description = "Erro ao criar conta")})
    @CacheEvict(cacheNames="accounts", key="#principal.name")
    @Override
    public AccountOutputDto create(@RequestBody @Valid AccountInputDto input, Principal principal) {
        final var username = principal.getName();
        final var account = saveAccountUseCase.createAccount(input.type(), input.initialBalance(), username);

        return AccountOutputDto.builder().accountId(account.getId()).username(username).type(account.getType())
                .balance(input.initialBalance()).build();
    }

    @GetMapping
    @Operation(summary = "Recuperar contas do usuário", description = "Retorna lista de contas do usuário autenticado", responses = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = AccountOutputDto.class)))})})
    @Override
    @Cacheable(value = "accounts", key = "#principal.name", unless = "#result.isEmpty()")
    public List<AccountOutputDto> findBy(Principal principal) {
        return readAccountsUseCase.readBy(principal.getName()).stream().map(account -> AccountOutputDto.builder()
                .accountId(account.getId())
                .username(principal.getName())
                .type(account.getType())
                .balance(account.getBalance())
                .build()).toList();
    }

    @PatchMapping("/{id}/deposit")
    @Operation(summary = "Fazer depósito", description = "Fazer uma transação de depósito", responses = {
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = AccountTransactionOutputDto.class))}),
            @ApiResponse(responseCode = "422", description = "Erro na transação")})
    @CacheEvict(cacheNames="balance", key = "#principal.name + ':' + #id")
    @Override
    public AccountTransactionOutputDto deposit(Long id, @RequestBody @Valid AccountTransactionInputDto input,
                                               Principal principal){
        final var account = depositUseCase.process(id, principal.getName(), input.amount());
        return AccountTransactionOutputDto.builder()
                .accountNumber(account.getId())
                .amount(input.amount())
                .remainingBalance(account.getBalance())
                .transactionDateTime(now())
                .build();
    }

    @PatchMapping("/{id}/withdrawal")
    @Operation(summary = "Fazer saque", description = "Fazer uma transação de saque", responses = {
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = AccountTransactionOutputDto.class))}),
            @ApiResponse(responseCode = "422", description = "Erro na transação")})
    @CacheEvict(cacheNames="balance", key = "#principal.name + ':' + #id")
    @Override
    public AccountTransactionOutputDto withdrawal(Long id, @RequestBody @Valid AccountTransactionInputDto input,
                                                  Principal principal) {
        final var account = withdrawalUseCase.process(id, principal.getName(), input.amount());
        return AccountTransactionOutputDto.builder()
                .accountNumber(account.getId())
                .amount(input.amount())
                .remainingBalance(account.getBalance())
                .transactionDateTime(now())
                .build();
    }

    @GetMapping("/{id}/balance")
    @Operation(summary = "Recuperar saldo de uma conta", description = "Retorna saldo de uma por id", responses = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = BigDecimal.class))}),
            @ApiResponse(responseCode = "404", description = "Conta não existente")})
    @Cacheable(value = "balance", key = "#principal.name + ':' + #id")
    @Override
    public AccountBalanceOutputDto balance(Long id, Principal principal) {
        final var builder = AccountBalanceOutputDto.builder();
        final var account = readBalanceUseCase.readBalanceWithHistory(id, principal.getName());
        builder.balance(account.getBalance());

        builder.history(account.getHistory().stream().map(item ->
                                                                  HistoryItemDto.builder().type(item.getType().name())
                                                                          .amount(item.getAmount().setScale(2,
                                                                                                            RoundingMode.DOWN))
                                                                          .date(now()).build()

        ).toList());
        return builder.build();
    }
}
