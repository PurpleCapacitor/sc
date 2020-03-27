package bank.erste.dto;

import java.io.Serializable;

public class CardDTO implements Serializable {

    private String pan;
    private String cardHolderName;
    private String validThru;
    private int ccv;
    private String paymentId;
    private long acquirerId;
    private String acquirerTimestamp;
    private long issuerOrderId;
    private String issuerTimestamp;
    private String amount;

    public CardDTO() {
    }

    public CardDTO(String pan, String cardHolderName, String validThru, int ccv, String paymentId) {
        this.pan = pan;
        this.cardHolderName = cardHolderName;
        this.validThru = validThru;
        this.ccv = ccv;
        this.paymentId = paymentId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public long getIssuerOrderId() {
        return issuerOrderId;
    }

    public void setIssuerOrderId(long issuerOrderId) {
        this.issuerOrderId = issuerOrderId;
    }

    public String getIssuerTimestamp() {
        return issuerTimestamp;
    }

    public void setIssuerTimestamp(String issuerTimestamp) {
        this.issuerTimestamp = issuerTimestamp;
    }

    public long getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(long acquirerId) {
        this.acquirerId = acquirerId;
    }

    public String getAcquirerTimestamp() {
        return acquirerTimestamp;
    }

    public void setAcquirerTimestamp(String acquirerTimestamp) {
        this.acquirerTimestamp = acquirerTimestamp;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getValidThru() {
        return validThru;
    }

    public void setValidThru(String validThru) {
        this.validThru = validThru;
    }

    public int getCcv() {
        return ccv;
    }

    public void setCcv(int ccv) {
        this.ccv = ccv;
    }
}
