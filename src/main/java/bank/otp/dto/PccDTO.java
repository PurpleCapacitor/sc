package bank.otp.dto;

import java.io.Serializable;

public class PccDTO implements Serializable {

    private String issuerOrderId;
    private String acquirerId;
    private String acquirerTimestamp;
    private String issuerTimestamp;
    private String status;

    public PccDTO() {

    }

    public PccDTO(String issuerOrderId, String acquirerId, String acquirerTimestamp, String issuerTimestamp, String status) {
        this.issuerOrderId = issuerOrderId;
        this.acquirerId = acquirerId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.issuerTimestamp = issuerTimestamp;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssuerOrderId() {
        return issuerOrderId;
    }

    public void setIssuerOrderId(String issuerOrderId) {
        this.issuerOrderId = issuerOrderId;
    }

    public String getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(String acquirerId) {
        this.acquirerId = acquirerId;
    }

    public String getAcquirerTimestamp() {
        return acquirerTimestamp;
    }

    public void setAcquirerTimestamp(String acquirerTimestamp) {
        this.acquirerTimestamp = acquirerTimestamp;
    }

    public String getIssuerTimestamp() {
        return issuerTimestamp;
    }

    public void setIssuerTimestamp(String issuerTimestamp) {
        this.issuerTimestamp = issuerTimestamp;
    }
}
