package br.com.darlansilva.bankapp.core.domain;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 2121926365899502417L;
    private final Long id;
    private final AccountType type;
    private BigDecimal balance;
    private final User user;
    private final List<TransactionHistoryItem> history;

     public Account(Long id, AccountType type, BigDecimal balance, User user, List<TransactionHistoryItem> history) {
        this.id = id;
        this.type = type;
        this.balance = balance.setScale(2, RoundingMode.DOWN);
        this.user = user;
        this.history = history;
    }

    public Long getId() {
        return id;
    }

    public AccountType getType() {
        return type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public User getUser() {
        return user;
    }

    public static Account from(AccountType type, BigDecimal balance, User user) {
        return new Account(null, type, balance, user, List.of());
    }

    public static Account from(Long id, AccountType type, BigDecimal balance, User user) {
        return new Account(id, type, balance, user, List.of());
    }

    public void deposit(BigDecimal amount) {
         if(this.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                final var fees = new BigDecimal("1.02")
                        .movePointLeft(2)
                        .setScale(2, RoundingMode.DOWN);

             final var fee = amount.abs().multiply(fees).setScale(2, RoundingMode.DOWN);
             final var total = amount.subtract(fee);
             this.balance = balance.add(total.setScale(2, RoundingMode.DOWN));
             return;
         }
        this.balance = this.balance.add(amount);
    }

    public void withdrawal(BigDecimal amount) {
        this.balance = this.balance.subtract(amount).setScale(2, RoundingMode.DOWN);
    }

    public void pay(BigDecimal amount) {
        this.balance = this.balance.subtract(amount).setScale(2, RoundingMode.DOWN);
    }

    public List<TransactionHistoryItem> getHistory() {
        return history;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Account that = (Account) o;
        return Objects.equals(id, that.id) && Objects.equals(balance, that.balance) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, user);
    }
}
