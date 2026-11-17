import java.util.Scanner;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import interfaces.PaymentMethod;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static PersonalFinanceManager manager;

    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("       PERSONAL FINANCE MANAGER");
        System.out.println("======================================");

        profileMenu();
    }

    public static void profileMenu() {
        while (true) {
            System.out.println("\n========== PROFILE MENU ==========");
            System.out.println("1. Create New Profile");
            System.out.println("2. Open Existing Profile");
            System.out.println("0. Exit");
            System.out.println("==================================");

            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        createProfile();
                        if (manager != null) {
                            showMenu();
                        }
                        break;

                    case 2:
                        if (openExistingProfile()) {
                            showMenu();
                        }
                        break;

                    case 0:
                        System.out.println("Thank you for using Personal Finance Manager!");
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static void createProfile() {
        manager = null;
        System.out.println("\n========== CREATE NEW PROFILE ==========");

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        if (email.trim().isEmpty()) {
            System.out.println("Email cannot be empty.");
            return;
        }

        if (ProfileStorage.emailExists(email)) {
            System.out.println("A profile with this email already exists.");
            return;
        }

        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();

        int userId = ProfileStorage.getNextUserId();

        User user = new User(userId, name, email, phone);
        manager = new PersonalFinanceManager(user);

        ProfileStorage.saveProfile(manager);

        System.out.println("\nProfile created successfully!");
        System.out.println("Your User ID is: " + userId);
        System.out.println("Welcome, " + user.getName() + "!");
    }

    public static boolean openExistingProfile() {
        ArrayList<PersonalFinanceManager> profiles =
            ProfileStorage.getAllProfiles();

        if (profiles.isEmpty()) {
            System.out.println("\nNo saved profiles found.");
            return false;
        }

        profiles.sort((first, second) ->
            Integer.compare(
                first.getUser().getUserId(),
                second.getUser().getUserId()
            )
        );

        System.out.println("\n========== SAVED PROFILES ==========");

        for (int i = 0; i < profiles.size(); i++) {
            User user = profiles.get(i).getUser();

            System.out.println((i + 1) + ". " + user.getName());
            System.out.println("   User ID: " + user.getUserId());
            System.out.println("   Email: " + user.getEmail());
            System.out.println("------------------------------------");
        }

        System.out.print("Choose a profile number: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice < 1 || choice > profiles.size()) {
                System.out.println("Invalid profile selection.");
                return false;
            }

            int userId = profiles.get(choice - 1).getUser().getUserId();

            manager = ProfileStorage.loadProfile(userId);

            if (manager == null) {
                return false;
            }

            System.out.println("Welcome back, " + manager.getUser().getName() + "!");
            return true;

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return false;
        }
    }

    public static void saveCurrentProfile() {
        if (manager != null) {
            ProfileStorage.saveProfile(manager);
        }
    }

    public static void showMenu() {
        int choice;

        do {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View All Transactions");
            System.out.println("4. Financial Summary");
            System.out.println("5. Category-wise Expenses");
            System.out.println("6. View Profile");
            System.out.println("7. Set Monthly Budget");
            System.out.println("8. View Budget Status");
            System.out.println("9. Search Transaction by ID");
            System.out.println("10. Filter by Transaction Type");
            System.out.println("11. Filter by Date");
            System.out.println("12. Filter by Expense Category");
            System.out.println("13. Filter by Payment Method");
            System.out.println("14. Edit Transaction");
            System.out.println("15. Delete Transaction");
            System.out.println("16. Switch Profile");
            System.out.println("0. Exit");
            System.out.println("===============================");

            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        addIncome();
                        break;

                    case 2:
                        addExpense();
                        break;

                    case 3:
                        manager.displayAllTransactions();
                        break;

                    case 4:
                        manager.generateReport().displayFinancialSummary();
                        break;

                    case 5:
                        manager.generateReport().displayCategoryWiseExpenses();
                        break;

                    case 6:
                        manager.getUser().displayUserDetails();
                        break;

                    case 7:
                        setMonthlyBudget();
                        break;

                    case 8:
                        viewBudgetStatus();
                        break;

                    case 9:
                        searchTransactionById();
                        break;

                    case 10:
                        filterByType();
                        break;

                    case 11:
                        filterByDate();
                        break;

                    case 12:
                        filterByCategory();
                        break;

                    case 13:
                        filterByPaymentMethod();
                        break;

                    case 14:
                        editTransaction();
                        break;

                    case 15:
                        deleteTransaction();
                        break;

                    case 16:
                        saveCurrentProfile();
                        System.out.println("Returning to Profile Menu...");
                        break;

                    case 0:
                        saveCurrentProfile();
                        System.out.println("Thank you for using Personal Finance Manager!");
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } catch (NumberFormatException e) {
                choice = -1;
                System.out.println("Please enter a valid number.");

            } catch (IllegalArgumentException e) {
                choice = -1;
                System.out.println("Error: " + e.getMessage());

            } catch (Exception e) {
                choice = -1;
                System.out.println("Something went wrong: " + e.getMessage());
            }

        } while (choice != 0 && choice != 16);
    }

    public static void addIncome() {
        System.out.println("\n========== ADD INCOME ==========");

        System.out.print("Enter amount: Rs. ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter income source: ");
        String source = scanner.nextLine();

        int id = manager.generateTransactionId();

        Income income = new Income(id, amount, date, description, source);

        manager.addTransaction(income);
        saveCurrentProfile();
    }

    public static void addExpense() {
        System.out.println("\n========== ADD EXPENSE ==========");

        System.out.print("Enter amount: Rs. ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        YearMonth expenseMonth = YearMonth.from(date);
        Budget monthlyBudget = manager.findBudgetByMonth(expenseMonth);

        if (monthlyBudget != null) {
            BigDecimal currentExpenses = manager.getMonthlyExpenses(expenseMonth);
            BigDecimal projectedExpenses = currentExpenses.add(amount);

            if (monthlyBudget.isExceeded(projectedExpenses)) {
                System.out.println("\n!!!!!!!!!!!!!!!! BUDGET WARNING !!!!!!!!!!!!!!!!");
                System.out.println("This expense will cause you to exceed your monthly budget.");
                System.out.println("Current monthly expenses: Rs. " + currentExpenses);
                System.out.println("New expense: Rs. " + amount);
                System.out.println("Total after this expense: Rs. " + projectedExpenses);
                System.out.println("Please review your spending before continuing.");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                System.out.print("Do you still want to add this expense? (Y/N): ");
                String confirmation = scanner.nextLine();

                if (!confirmation.trim().equalsIgnoreCase("Y")) {
                    System.out.println("Expense cancelled. No transaction was added.");
                    return;
                }
            }
        }

        System.out.println("\nSelect Expense Category:");

        ExpenseCategory[] categories = ExpenseCategory.values();

        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i].getDisplayName());
        }

        System.out.print("Enter category number: ");
        int categoryChoice = Integer.parseInt(scanner.nextLine());

        if (categoryChoice < 1 || categoryChoice > categories.length) {
            throw new IllegalArgumentException("Invalid category selection.");
        }

        ExpenseCategory category = categories[categoryChoice - 1];

        PaymentMethod paymentMethod = selectPaymentMethod();

        int id = manager.generateTransactionId();

        Expense expense = new Expense(
            id, amount, date, description, category, paymentMethod
        );

        manager.addTransaction(expense);
        saveCurrentProfile();
    }

    public static PaymentMethod selectPaymentMethod() {
        System.out.println("\nSelect Payment Method:");
        System.out.println("1. Cash");
        System.out.println("2. UPI");
        System.out.println("3. Card");

        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                return new Cash();

            case 2:
                System.out.print("Enter your UPI ID: ");
                String upiId = scanner.nextLine();
                return new UPI(upiId);

            case 3:
                System.out.print("Enter card type (Visa, Mastercard, etc.): ");
                String cardType = scanner.nextLine();

                System.out.print("Enter last four digits of card: ");
                String lastFourDigits = scanner.nextLine();

                return new Card(cardType, lastFourDigits);

            default:
                throw new IllegalArgumentException("Invalid payment method.");
        }
    }

    public static void setMonthlyBudget() {
        System.out.println("\n========== SET MONTHLY BUDGET ==========");

        System.out.print("Enter month (YYYY-MM): ");
        YearMonth month = YearMonth.parse(scanner.nextLine());

        System.out.print("Enter budget limit: Rs. ");
        BigDecimal limit = new BigDecimal(scanner.nextLine());

        int budgetId = manager.getBudgets().size() + 1;

        Budget budget = new Budget(budgetId, month, limit);

        manager.addBudget(budget);
        saveCurrentProfile();
    }

    public static void viewBudgetStatus() {
        System.out.println("\n========== VIEW BUDGET STATUS ==========");

        System.out.print("Enter month (YYYY-MM): ");
        YearMonth month = YearMonth.parse(scanner.nextLine());

        manager.displayBudgetStatus(month);
    }

    public static void searchTransactionById() {
        System.out.print("Enter transaction ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        Transaction transaction = manager.findTransactionById(id);

        if (transaction == null) {
            System.out.println("Transaction not found.");
        } else {
            transaction.displayTransaction();
        }
    }

    public static void filterByType() {
        System.out.println("1. Income");
        System.out.println("2. Expense");

        System.out.print("Choose transaction type: ");
        int choice = Integer.parseInt(scanner.nextLine());

        String type;

        if (choice == 1) {
            type = "Income";
        } else if (choice == 2) {
            type = "Expense";
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        ArrayList<Transaction> results = manager.searchByType(type);
        manager.displaySearchResults(results);
    }

    public static void filterByDate() {
        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        ArrayList<Transaction> results = manager.searchByDate(date);
        manager.displaySearchResults(results);
    }

    public static void filterByCategory() {
        ExpenseCategory[] categories = ExpenseCategory.values();

        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i].getDisplayName());
        }

        System.out.print("Choose category: ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice < 1 || choice > categories.length) {
            System.out.println("Invalid category.");
            return;
        }

        ExpenseCategory category = categories[choice - 1];

        ArrayList<Transaction> results = manager.searchByCategory(category);
        manager.displaySearchResults(results);
    }

    public static void filterByPaymentMethod() {
        System.out.println("1. Cash");
        System.out.println("2. UPI");
        System.out.println("3. Card");

        System.out.print("Choose payment method: ");
        int choice = Integer.parseInt(scanner.nextLine());

        String method;

        if (choice == 1) {
            method = "Cash";
        } else if (choice == 2) {
            method = "UPI";
        } else if (choice == 3) {
            method = "Card";
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        ArrayList<Transaction> results = manager.searchByPaymentMethod(method);
        manager.displaySearchResults(results);
    }

    public static void editTransaction() {
        System.out.print("Enter transaction ID to edit: ");
        int id = Integer.parseInt(scanner.nextLine());

        Transaction transaction = manager.findTransactionById(id);

        if (transaction == null) {
            System.out.println("Transaction not found.");
            return;
        }

        transaction.displayTransaction();

        System.out.println("\nEnter the updated details.");

        System.out.print("Enter new amount: Rs. ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        System.out.print("Enter new date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter new description: ");
        String description = scanner.nextLine();

        if (transaction instanceof Income) {
            System.out.print("Enter new income source: ");
            String source = scanner.nextLine();

            boolean updated = manager.updateIncome(
                id, amount, date, description, source
            );

            if (updated) {
                System.out.println("Income updated successfully!");
                saveCurrentProfile();
            }

        } else if (transaction instanceof Expense) {
            ExpenseCategory[] categories = ExpenseCategory.values();

            for (int i = 0; i < categories.length; i++) {
                System.out.println((i + 1) + ". " + categories[i].getDisplayName());
            }

            System.out.print("Enter new category number: ");
            int categoryChoice = Integer.parseInt(scanner.nextLine());

            if (categoryChoice < 1 || categoryChoice > categories.length) {
                throw new IllegalArgumentException("Invalid category selection.");
            }

            ExpenseCategory category = categories[categoryChoice - 1];

            PaymentMethod paymentMethod = selectPaymentMethod();

            boolean updated = manager.updateExpense(
                id, amount, date, description, category, paymentMethod
            );

            if (updated) {
                System.out.println("Expense updated successfully!");
                saveCurrentProfile();
            }
        }
    }

    public static void deleteTransaction() {
        System.out.print("Enter transaction ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        boolean deleted = manager.deleteTransaction(id);

        if (deleted) {
            System.out.println("Transaction deleted successfully!");
            saveCurrentProfile();
        } else {
            System.out.println("Transaction not found.");
        }
    }
}
