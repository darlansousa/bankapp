package br.com.darlansilva.bankapp.entrypoint.controller.auth;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.darlansilva.bankapp.entrypoint.dto.AuthRequest;
import br.com.darlansilva.bankapp.entrypoint.dto.AuthResponse;

@RestController
@RequestMapping("/auth")
public interface AuthController {


    ResponseEntity<AuthResponse> login(AuthRequest req);

}
