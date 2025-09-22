package br.com.darlansilva.bankapp.entrypoint.api.dto.input;

import jakarta.validation.constraints.NotNull;

public record AuthInputFormDto(@NotNull String username, @NotNull String password) {
}
