package br.com.darlansilva.bankapp.entrypoint.api.controller.account.impl;

import static br.com.darlansilva.bankapp.core.domain.AccountType.CHECKING;
import static br.com.darlansilva.bankapp.core.domain.TransactionHistoryItem.depositInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.darlansilva.bankapp.core.domain.Account;
import br.com.darlansilva.bankapp.core.usecase.account.DepositUseCase;
import br.com.darlansilva.bankapp.core.usecase.account.ReadAccountsUseCase;
import br.com.darlansilva.bankapp.core.usecase.account.ReadBalanceUseCase;
import br.com.darlansilva.bankapp.core.usecase.account.SaveAccountUseCase;
import br.com.darlansilva.bankapp.core.usecase.account.WithdrawalUseCase;
import br.com.darlansilva.bankapp.entrypoint.api.config.ApiExceptionHandler;
import br.com.darlansilva.bankapp.entrypoint.api.dto.input.AccountInputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.input.AccountTransactionInputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.AccountBalanceOutputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.AccountOutputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.AccountTransactionOutputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.HistoryItemDto;

@ExtendWith(MockitoExtension.class)
class AccountControllerV1ImplTest {

    private static final EasyRandom EASY_RANDOM = new EasyRandom();
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private MockMvc mvc;

    @InjectMocks
    private AccountControllerV1Impl subject;

    @Mock
    private SaveAccountUseCase saveAccountUseCaseMock;
    @Mock
    private ReadBalanceUseCase readBalanceUseCaseMock;
    @Mock
    private WithdrawalUseCase withdrawalUseCaseMock;
    @Mock
    private DepositUseCase depositUseCaseMock;
    @Mock
    private ReadAccountsUseCase readAccountsUseCaseMock;

    @BeforeEach
    void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(subject)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void shouldCreateAccount() throws Exception {
        final var username = EASY_RANDOM.nextObject(String.class);
        final var password = EASY_RANDOM.nextObject(String.class);

        final var input = new AccountInputDto(CHECKING, EASY_RANDOM.nextObject(BigDecimal.class));
        final var created = EASY_RANDOM.nextObject(Account.class);

        given(saveAccountUseCaseMock.createAccount(input.type(), input.initialBalance(), username)).willReturn(created);

        final var expected = AccountOutputDto.builder()
                .accountId(created.getId())
                .username(username)
                .type(created.getType())
                .balance(input.initialBalance())
                .build();

        final var response = mvc.perform(
                        post("/v1/accounts")
                                .principal(() -> username)
                                .with(user(username).password(password))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(input))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(mapper.writeValueAsString(expected), response.getContentAsString());

        then(saveAccountUseCaseMock).should().createAccount(input.type(), input.initialBalance(), username);
    }

    @Test
    void shouldListUserAccounts() throws Exception {
        final var username = EASY_RANDOM.nextObject(String.class);
        final var password = EASY_RANDOM.nextObject(String.class);

        final var a1 = EASY_RANDOM.nextObject(Account.class);
        final var a2 = EASY_RANDOM.nextObject(Account.class);
        final var accounts = List.of(a1, a2);

        given(readAccountsUseCaseMock.readBy(username)).willReturn(accounts);

        final var expected = List.of(
                AccountOutputDto.builder()
                        .accountId(a1.getId())
                        .username(username)
                        .type(a1.getType())
                        .balance(a1.getBalance())
                        .build(),
                AccountOutputDto.builder()
                        .accountId(a2.getId())
                        .username(username)
                        .type(a2.getType())
                        .balance(a2.getBalance())
                        .build()
        );

        final var response = mvc.perform(
                        get("/v1/accounts")
                                .principal(() -> username)
                                .with(user(username).password(password))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(mapper.writeValueAsString(expected), response.getContentAsString());

        then(readAccountsUseCaseMock).should().readBy(username);
    }

    @Test
    void shouldReturnBalance() throws Exception {
        final var accountId = EASY_RANDOM.nextObject(Long.class);
        final var username = EASY_RANDOM.nextObject(String.class);
        final var password = EASY_RANDOM.nextObject(String.class);

        final var account = EASY_RANDOM.nextObject(Account.class);

        given(readBalanceUseCaseMock.readBalanceWithHistory(accountId, username)).willReturn(account);

        final var expected =
                AccountBalanceOutputDto.builder()
                        .balance(account.getBalance())
                        .history(account.getHistory().stream().map(item ->
                                            HistoryItemDto.builder()
                                                    .type(item.getType().name())
                                        .amount(item.getAmount().setScale(2, RoundingMode.DOWN))
                                                    .date(item.getCreated())
                                        .build()
                        ).toList())
                        .build();

        final var response = mvc.perform(
                        get("/v1/accounts/" + accountId + "/balance")
                                .principal(() -> username)
                                .with(user(username).password(password))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(mapper.writeValueAsString(expected), response.getContentAsString());

        then(readBalanceUseCaseMock).should().readBalanceWithHistory(accountId, username);
    }

    @Test
    void shouldDeposit() throws Exception {
        final var accountId = EASY_RANDOM.nextObject(Long.class);
        final var username = EASY_RANDOM.nextObject(String.class);
        final var password = EASY_RANDOM.nextObject(String.class);
        final var amount = BigDecimal.valueOf(1.50).setScale(2, RoundingMode.DOWN);
        final var depositedValue = BigDecimal.valueOf(100.50).setScale(2, RoundingMode.DOWN);
        final var history = depositInstance(amount, depositedValue);
        final var input = new AccountTransactionInputDto(amount);

        final var account = new Account(accountId, CHECKING, depositedValue.add(amount), null, List.of(history));

        given(depositUseCaseMock.process(accountId, username, amount)).willReturn(account);

        final var expected = AccountTransactionOutputDto.builder()
                .accountNumber(accountId)
                .transactionDateTime(LocalDateTime.now())
                .remainingBalance(depositedValue.add(amount))
                .amount(amount)
                .build();

        final var response = mvc.perform(
                        patch("/v1/accounts/" + accountId + "/deposit")
                                .principal(() -> username)
                                .with(user(username).password(password))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(input))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(mapper.writeValueAsString(expected), response.getContentAsString());

        then(depositUseCaseMock).should().process(accountId, username, amount);
    }

    @Test
    void shouldWithdrawal() throws Exception {
        final var accountId = EASY_RANDOM.nextObject(Long.class);
        final var username = EASY_RANDOM.nextObject(String.class);
        final var password = EASY_RANDOM.nextObject(String.class);
        final var amount = BigDecimal.valueOf(1.50).setScale(2, RoundingMode.DOWN);
        final var depositedValue = BigDecimal.valueOf(100.50).setScale(2, RoundingMode.DOWN);
        final var history = depositInstance(amount, depositedValue);
        final var input = new AccountTransactionInputDto(amount);

        final var account = new Account(accountId, CHECKING, depositedValue.subtract(amount), null, List.of(history));

        given(withdrawalUseCaseMock.process(accountId, username, amount)).willReturn(account);

        final var expected = AccountTransactionOutputDto.builder()
                .accountNumber(accountId)
                .transactionDateTime(LocalDateTime.now())
                .remainingBalance(depositedValue.subtract(amount))
                .amount(amount)
                .build();

        final var response = mvc.perform(
                        patch("/v1/accounts/" + accountId + "/withdrawal")
                                .principal(() -> username)
                                .with(user(username).password(password))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(input))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(mapper.writeValueAsString(expected), response.getContentAsString());

        then(withdrawalUseCaseMock).should().process(accountId, username, amount);
    }

}