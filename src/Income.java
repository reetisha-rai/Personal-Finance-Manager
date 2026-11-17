import java.math.BigDecimal;
import java.time.LocalDate;

public class Income extends Transaction {

    private String source;

    public Income(int transactionId, BigDecimal amount, LocalDate date, String description, String source) {
        super(transactionId, amount, date, description);
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String getTransactionType() {
        return "Income";
    }

    @Override
    public void displayTransaction() {
        super.displayTransaction();
        System.out.println("Income Source: " + source);
    }
}
