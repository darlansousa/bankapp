package br.com.darlansilva.bankapp.core.domain;


public class Authority {
    private final Long id;
    private final User user;
    private final UserRole role;

    public Authority(Long id, User user, UserRole role) {
        this.id = id;
        this.user = user;
        this.role = role;
    }

    public static Authority from(Long id, User user, UserRole role) {
        return new Authority(id, user, role);
    }

    public static Authority from(User user, UserRole role) {
        return new Authority(null, user, role);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public UserRole getRole() {
        return role;
    }

}
