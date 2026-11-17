import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Report {

    private ArrayList<Transaction> transactions;

    public Report(ArrayList<Transaction> transactions) {
        if (transactions == null) {
            throw new IllegalArgumentException("Transaction list cannot be null.");
        }
        this.transactions = transactions;
    }

    public BigDecimal getTotalIncome() {
        BigDecimal total = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (transaction instanceof Income) {
                total = total.add(transaction.getAmount());
            }
        }

        return total;
    }

    public BigDecimal getTotalExpenses() {
        BigDecimal total = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (transaction instanceof Expense) {
                total = total.add(transaction.getAmount());
            }
        }

        return total;
    }

    public BigDecimal getAvailableBalance() {
        return getTotalIncome().subtract(getTotalExpenses());
    }

    public BigDecimal getMonthlyIncome(YearMonth month) {
        BigDecimal total = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (transaction instanceof Income &&
                    YearMonth.from(transaction.getDate()).equals(month)) {
                total = total.add(transaction.getAmount());
            }
        }

        return total;
    }

    public BigDecimal getMonthlyExpenses(YearMonth month) {
        BigDecimal total = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (transaction instanceof Expense &&
                    YearMonth.from(transaction.getDate()).equals(month)) {
                total = total.add(transaction.getAmount());
            }
        }

        return total;
    }

    public BigDecimal getMonthlySavings(YearMonth month) {
        return getMonthlyIncome(month).subtract(getMonthlyExpenses(month));
    }

    public HashMap<ExpenseCategory, BigDecimal> getCategoryWiseExpenses() {
        HashMap<ExpenseCategory, BigDecimal> categoryExpenses = new HashMap<>();

        for (ExpenseCategory category : ExpenseCategory.values()) {
            categoryExpenses.put(category, BigDecimal.ZERO);
        }

        for (Transaction transaction : transactions) {
            if (transaction instanceof Expense) {
                Expense expense = (Expense) transaction;

                ExpenseCategory category = expense.getCategory();

                BigDecimal currentAmount = categoryExpenses.get(category);

                categoryExpenses.put(
                    category,
                    currentAmount.add(expense.getAmount())
                );
            }
        }

        return categoryExpenses;
    }

    public void displayFinancialSummary() {
        System.out.println("\n========== FINANCIAL SUMMARY ==========");
        System.out.println("Total Income: Rs. " + getTotalIncome());
        System.out.println("Total Expenses: Rs. " + getTotalExpenses());
        System.out.println("Available Balance: Rs. " + getAvailableBalance());
        System.out.println("=======================================");
    }

    public void displayCategoryWiseExpenses() {
        System.out.println("\n======= CATEGORY-WISE EXPENSES =======");

        HashMap<ExpenseCategory, BigDecimal> categoryExpenses =
                getCategoryWiseExpenses();

        for (Map.Entry<ExpenseCategory, BigDecimal> entry : categoryExpenses.entrySet()) {
            System.out.println(
                entry.getKey().getDisplayName() + ": Rs. " + entry.getValue()
            );
        }

        System.out.println("======================================");
    }

    public void displayMonthlySummary(YearMonth month) {
        System.out.println("\n========== MONTHLY SUMMARY ==========");
        System.out.println("Month: " + month);
        System.out.println("Income: Rs. " + getMonthlyIncome(month));
        System.out.println("Expenses: Rs. " + getMonthlyExpenses(month));
        System.out.println("Savings: Rs. " + getMonthlySavings(month));
        System.out.println("=====================================");
    }
}
