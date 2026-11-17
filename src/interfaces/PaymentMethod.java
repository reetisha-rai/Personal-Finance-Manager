package interfaces;

import java.io.Serializable;

public interface PaymentMethod extends Serializable {

    String getMethodName();

    String getPaymentDetails();
}
