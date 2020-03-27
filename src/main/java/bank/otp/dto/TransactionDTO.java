package bank.otp.dto;

import java.io.Serializable;

public class TransactionDTO implements Serializable {

    private long merchantOrderId;
    private long paymentId;
    private String url;
    private String status;

    public TransactionDTO() {

    }

    public long getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(long merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
