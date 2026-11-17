
public enum ExpenseCategory {

    FOOD("Food"),
    TRAVEL("Travel"),
    SHOPPING("Shopping"),
    EDUCATION("Education"),
    BILLS("Bills"),
    ENTERTAINMENT("Entertainment"),
    HEALTH("Health"),
    OTHER("Other");

    private final String displayName;

    ExpenseCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static void displayCategories() {
        for (ExpenseCategory category : ExpenseCategory.values()) {
            System.out.println(category.ordinal() + ". " + category.getDisplayName());
        }
    }
}
