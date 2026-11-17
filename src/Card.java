import interfaces.PaymentMethod;

public class Card implements PaymentMethod {

    private String cardType;
    private String lastFourDigits;

    public Card(String cardType, String lastFourDigits) {
        setCardType(cardType);
        setLastFourDigits(lastFourDigits);
    }

    public String getCardType() {
        return cardType;
    }

    public String getLastFourDigits() {
        return lastFourDigits;
    }

    public void setCardType(String cardType) {
        if (cardType == null || cardType.trim().isEmpty()) {
            throw new IllegalArgumentException("Card type cannot be empty.");
        }
        this.cardType = cardType;
    }

    public void setLastFourDigits(String lastFourDigits) {
        if (lastFourDigits == null || !lastFourDigits.matches("\\d{4}")) {
            throw new IllegalArgumentException("Enter exactly four digits.");
        }
        this.lastFourDigits = lastFourDigits;
    }

    @Override
    public String getMethodName() {
        return "Card";
    }

    @Override
    public String getPaymentDetails() {
        return cardType + " card ending in " + lastFourDigits;
    }
}

