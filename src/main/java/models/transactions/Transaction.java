package models.transactions;

import enums.TranType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transaction(
        Long id,
        LocalDateTime date,
        double amount,
        TranType type,
        String location,
        Long accountId
) {
    public Transaction(LocalDateTime date, double amount, TranType type, String location, Long accountId) {
        this(null, date, amount, type, location, accountId);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Transaction{id=" + id + ", date=" + date.format(formatter) +
                ", amount=" + amount + ", type=" + type +
                ", location='" + location + "', accountId=" + accountId + "}";
    }
}