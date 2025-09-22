package br.com.darlansilva.bankapp.core.domain;

import static br.com.darlansilva.bankapp.core.domain.TransactionType.DEPOSIT;
import static br.com.darlansilva.bankapp.core.domain.TransactionType.INITIAL_BALANCE;
import static br.com.darlansilva.bankapp.core.domain.TransactionType.PAYMENT;
import static br.com.darlansilva.bankapp.core.domain.TransactionType.WITHDRAWAL;
import static java.math.BigDecimal.ZERO;
import static java.time.LocalDateTime.now;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class TransactionHistoryItem {

    private final Long id;
    private final TransactionType type;
    private final BigDecimal amount;
    private final BigDecimal balanceBefore;
    private final BigDecimal balanceAfter;
    private final String referenceId;
    private final LocalDateTime created;

    public TransactionHistoryItem(Long id, TransactionType type, BigDecimal amount,
                                   BigDecimal balanceBefore, BigDecimal balanceAfter,
                                   String referenceId, LocalDateTime created) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.referenceId = referenceId;
        this.created = created;
    }

    public static TransactionHistoryItem init(BigDecimal balance) {
        return new TransactionHistoryItem(null, INITIAL_BALANCE, balance, ZERO, balance,null,  now());
    }

    public static TransactionHistoryItem depositInstance(BigDecimal amount, BigDecimal balance) {
        return new TransactionHistoryItem(null, DEPOSIT, amount, balance, balance.add(amount),null,  now());
    }


    public static TransactionHistoryItem withdrawalInstance(BigDecimal amount, BigDecimal balance) {
        return new TransactionHistoryItem(null, WITHDRAWAL, amount, balance, balance.subtract(amount), null, now());
    }

    public static TransactionHistoryItem paymentInstance(BigDecimal amount, BigDecimal balance, String referenceId) {
        return new TransactionHistoryItem(null, PAYMENT, amount, balance, balance.subtract(amount), referenceId, now());
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getReferenceId() {
        return referenceId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransactionHistoryItem that = (TransactionHistoryItem) o;
        return Objects.equals(id, that.id) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
