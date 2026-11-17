import interfaces.PaymentMethod;

public class UPI implements PaymentMethod {

    private String upiId;

    public UPI(String upiId) {
        setUpiId(upiId);
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        if (upiId == null || !upiId.matches("[A-Za-z0-9._-]+@[A-Za-z0-9]+")) {
            throw new IllegalArgumentException("Invalid UPI ID.");
        }
        this.upiId = upiId;
    }

    @Override
    public String getMethodName() {
        return "UPI";
    }

    @Override
    public String getPaymentDetails() {
        return "Payment made using UPI ID: " + upiId;
    }
}
