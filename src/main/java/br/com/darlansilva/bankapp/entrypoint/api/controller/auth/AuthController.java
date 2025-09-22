package br.com.darlansilva.bankapp.entrypoint.api.controller.auth;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.darlansilva.bankapp.entrypoint.api.dto.input.AuthInputFormDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.AuthOutputDto;

@RestController
@RequestMapping("/auth")
public interface AuthController {


    ResponseEntity<AuthOutputDto> login(AuthInputFormDto req);

}
