package br.com.darlansilva.bankapp.entrypoint.api.controller.account;

import java.security.Principal;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import br.com.darlansilva.bankapp.entrypoint.api.dto.input.AccountInputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.input.AccountTransactionInputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.AccountBalanceOutputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.AccountOutputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.AccountTransactionOutputDto;

public interface AccountController {

    AccountOutputDto create(AccountInputDto accountInputDto, Principal principal);

    List<AccountOutputDto> findBy(Principal principal);

    AccountTransactionOutputDto deposit(Long id, AccountTransactionInputDto accountTransactionInputDto, Principal principal) throws
            AccountNotFoundException;

    AccountTransactionOutputDto withdrawal(Long id,AccountTransactionInputDto accountTransactionInputDto, Principal principal) throws
            AccountNotFoundException;

    AccountBalanceOutputDto balance(Long id, Principal principal) throws AccountNotFoundException;

}
