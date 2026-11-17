
import java.math.BigDecimal;
import java.time.LocalDate;
import interfaces.PaymentMethod;

public class Expense extends Transaction {

    private ExpenseCategory category;
    private PaymentMethod paymentMethod;

    public Expense(int transactionId, BigDecimal amount, LocalDate date, String description, ExpenseCategory category, PaymentMethod paymentMethod) {
        super(transactionId, amount, date, description);
        setCategory(category);
        setPaymentMethod(paymentMethod);
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setCategory(ExpenseCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
        this.category = category;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method cannot be empty.");
        }
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String getTransactionType() {
        return "Expense";
    }

    @Override
    public void displayTransaction() {
        super.displayTransaction();
        System.out.println("Category: " + category.getDisplayName());
        System.out.println("Payment Method: " + paymentMethod.getMethodName());
        System.out.println("Payment Details: " + paymentMethod.getPaymentDetails());
    }
}
