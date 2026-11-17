import interfaces.PaymentMethod;

public class Cash implements PaymentMethod {

    @Override
    public String getMethodName() {
        return "Cash";
    }

    @Override
    public String getPaymentDetails() {
        return "Payment made using cash.";
    }
}
