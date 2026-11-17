
import java.math.BigDecimal;
import java.time.LocalDate;
import java.io.Serializable;

public abstract class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    private int transactionId;
    private BigDecimal amount;
    private LocalDate date;
    private String description;

    public Transaction(int transactionId, BigDecimal amount, LocalDate date, String description) {
        this.transactionId = transactionId;
        setAmount(amount);
        setDate(date);
        setDescription(description);
    }

    public int getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void setAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        if (date == null || date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid transaction date.");
        }
        this.date = date;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
        this.description = description;
    }

    public abstract String getTransactionType();

    public void displayTransaction() {
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Type: " + getTransactionType());
        System.out.println("Amount: Rs. " + amount);
        System.out.println("Date: " + date);
        System.out.println("Description: " + description);
    }
}
