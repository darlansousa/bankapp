package br.com.darlansilva.bankapp.entrypoint.controller.auth.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.darlansilva.bankapp.entrypoint.controller.auth.AuthController;
import br.com.darlansilva.bankapp.entrypoint.dto.AuthRequest;
import br.com.darlansilva.bankapp.entrypoint.dto.AuthResponse;
import br.com.darlansilva.bankapp.infra.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    @PostMapping("/login")
    @Override
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest req) {
        var authentication = new UsernamePasswordAuthenticationToken(req.username(), req.password());
        authManager.authenticate(authentication);
        var user = userDetailsService.loadUserByUsername(req.username());
        var token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, "Bearer"));
    }
}
