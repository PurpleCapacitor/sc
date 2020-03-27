package bank.otp.dto;

import java.io.Serializable;

public class BankResponseDTO implements Serializable {

    private String paymentUrl;
    private String paymentId;
    private String finalUrl;
    private String merchantOrderId;

    public BankResponseDTO() {

    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
