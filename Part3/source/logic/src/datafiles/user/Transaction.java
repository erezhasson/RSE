package datafiles.user;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final double transactionAmount;
    private final double balanceAfterTransaction;
    private final TransactionType type;
    private final String timestamp;

    public Transaction(double transactionAmount, double balanceAfterTransaction, TransactionType type) {
        this.transactionAmount = transactionAmount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.type = type;
        this.timestamp = DateTimeFormatter.ofPattern("HH:mm:ss:SSS").format(LocalDateTime.now());
    }

    public double getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public enum TransactionType {
        STOCK_PURCHASE, STOCK_SELL, BALANCE_CHARGE, FILE_UPLOAD;
    }
}
