
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.Serializable;

import interfaces.PaymentMethod;

import java.math.BigDecimal;
import java.time.YearMonth;

public class PersonalFinanceManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private User user;
    private ArrayList<Transaction> transactions;
    private ArrayList<Budget> budgets;
    private int nextTransactionId;

    public PersonalFinanceManager(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        this.user = user;
        this.transactions = new ArrayList<>();
        this.budgets = new ArrayList<>();
        this.nextTransactionId = 1;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public ArrayList<Budget> getBudgets() {
        return budgets;
    }

    public int generateTransactionId() {
        return nextTransactionId++;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null.");
        }

        transactions.add(transaction);

        System.out.println("Transaction added successfully!");
        System.out.println("Your Transaction ID is: " + transaction.getTransactionId());
    }

    public Transaction findTransactionById(int transactionId) {
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionId() == transactionId) {
                return transaction;
            }
        }

        return null;
    }

    public boolean deleteTransaction(int transactionId) {
        Transaction transaction = findTransactionById(transactionId);

        if (transaction == null) {
            return false;
        }

        transactions.remove(transaction);
        return true;
    }

    public void displayAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\n========== ALL TRANSACTIONS ==========");

        for (Transaction transaction : transactions) {
            transaction.displayTransaction();
            System.out.println("--------------------------------------");
        }
    }

    public Report generateReport() {
        return new Report(transactions);
    }

    public void addBudget(Budget budget) {
        if (budget == null) {
            throw new IllegalArgumentException("Budget cannot be null.");
        }

        for (Budget existingBudget : budgets) {
            if (existingBudget.getMonth().equals(budget.getMonth())) {
                throw new IllegalArgumentException("A budget already exists for this month.");
            }
        }

        budgets.add(budget);
        System.out.println("Budget added successfully!");
    }

    public Budget findBudgetByMonth(YearMonth month) {
        for (Budget budget : budgets) {
            if (budget.getMonth().equals(month)) {
                return budget;
            }
        }

        return null;
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

    public void displayBudgetStatus(YearMonth month) {
        Budget budget = findBudgetByMonth(month);

        if (budget == null) {
            System.out.println("No budget set for " + month);
            return;
        }

        BigDecimal expenses = getMonthlyExpenses(month);

        budget.displayBudget(expenses);

        if (budget.isExceeded(expenses)) {
            System.out.println("Warning: You have exceeded your monthly budget!");
        } else if (budget.isApproachingLimit(expenses)) {
            System.out.println("Warning: You are approaching your monthly budget limit!");
        }
    }

    public ArrayList<Transaction> searchByType(String type) {
        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType().equalsIgnoreCase(type)) {
                results.add(transaction);
            }
        }

        return results;
    }

    public ArrayList<Transaction> searchByDate(LocalDate date) {
        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getDate().equals(date)) {
                results.add(transaction);
            }
        }

        return results;
    }

    public ArrayList<Transaction> searchByCategory(ExpenseCategory category) {
        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction instanceof Expense) {
                Expense expense = (Expense) transaction;

                if (expense.getCategory() == category) {
                    results.add(transaction);
                }
            }
        }

        return results;
    }

    public ArrayList<Transaction> searchByPaymentMethod(String method) {
        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction instanceof Expense) {
                Expense expense = (Expense) transaction;

                if (expense.getPaymentMethod().getMethodName().equalsIgnoreCase(method)) {
                    results.add(transaction);
                }
            }
        }

        return results;
    }

    public void displaySearchResults(ArrayList<Transaction> results) {
        if (results.isEmpty()) {
            System.out.println("No matching transactions found.");
            return;
        }

        System.out.println("\n========== SEARCH RESULTS ==========");

        for (Transaction transaction : results) {
            transaction.displayTransaction();
            System.out.println("--------------------------------------");
        }
    }

    public boolean updateIncome(int transactionId, BigDecimal amount,
                                LocalDate date, String description, String source) {

        Transaction transaction = findTransactionById(transactionId);

        if (!(transaction instanceof Income)) {
            return false;
        }

        Income income = (Income) transaction;

        income.setAmount(amount);
        income.setDate(date);
        income.setDescription(description);
        income.setSource(source);

        return true;
    }

    public boolean updateExpense(int transactionId, BigDecimal amount,
                                 LocalDate date, String description,
                                 ExpenseCategory category, PaymentMethod paymentMethod) {

        Transaction transaction = findTransactionById(transactionId);

        if (!(transaction instanceof Expense)) {
            return false;
        }

        Expense expense = (Expense) transaction;

        expense.setAmount(amount);
        expense.setDate(date);
        expense.setDescription(description);
        expense.setCategory(category);
        expense.setPaymentMethod(paymentMethod);

        return true;
    }
}
