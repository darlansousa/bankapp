package br.com.darlansilva.bankapp.core.domain;

import java.util.List;
import java.util.Objects;

public class User {
    private final Long id;
    private final String username;
    private final String cpf;
    private final String password;
    private final List<UserRole> roles;

    public User(Long id, String username, String cpf, String password, List<UserRole> roles) {
        this.id = id;
        this.username = username;
        this.cpf = cpf;
        this.password = password;
        this.roles = roles;
    }

    public static User from(String username, String cpf, String password, List<UserRole> roles) {
        return new User(null, username, cpf, password, roles);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public String getCpf() {
        return cpf;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, roles);
    }
}
