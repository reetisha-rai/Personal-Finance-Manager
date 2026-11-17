
import java.math.BigDecimal;
import java.time.YearMonth;
import java.io.Serializable;

public class Budget implements Serializable {

    private static final long serialVersionUID = 1L;

    private int budgetId;
    private YearMonth month;
    private BigDecimal budgetLimit;

    public Budget(int budgetId, YearMonth month, BigDecimal budgetLimit) {
        this.budgetId = budgetId;
        setMonth(month);
        setBudgetLimit(budgetLimit);
    }

    public int getBudgetId() {
        return budgetId;
    }

    public YearMonth getMonth() {
        return month;
    }

    public BigDecimal getBudgetLimit() {
        return budgetLimit;
    }

    public void setMonth(YearMonth month) {
        if (month == null) {
            throw new IllegalArgumentException("Month cannot be null.");
        }
        this.month = month;
    }

    public void setBudgetLimit(BigDecimal budgetLimit) {
        if (budgetLimit == null || budgetLimit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Budget limit must be greater than zero.");
        }
        this.budgetLimit = budgetLimit;
    }

    public BigDecimal getRemainingAmount(BigDecimal totalExpenses) {
        return budgetLimit.subtract(totalExpenses);
    }

    public boolean isExceeded(BigDecimal totalExpenses) {
        return totalExpenses.compareTo(budgetLimit) > 0;
    }

    public boolean isApproachingLimit(BigDecimal totalExpenses) {
        BigDecimal threshold = budgetLimit.multiply(new BigDecimal("0.80"));

        return totalExpenses.compareTo(threshold) >= 0
                && totalExpenses.compareTo(budgetLimit) <= 0;
    }

    public void displayBudget(BigDecimal totalExpenses) {
        System.out.println("\n========== BUDGET DETAILS ==========");
        System.out.println("Month: " + month);
        System.out.println("Budget Limit: Rs. " + budgetLimit);
        System.out.println("Total Expenses: Rs. " + totalExpenses);
        System.out.println("Remaining Amount: Rs. " + getRemainingAmount(totalExpenses));

        if (isExceeded(totalExpenses)) {
            System.out.println("Status: Exceeded");
        } else if (isApproachingLimit(totalExpenses)) {
            System.out.println("Status: Approaching Limit");
        } else {
            System.out.println("Status: Within Budget");
        }

        System.out.println("====================================");
    }
}
