package payment.pcc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Bank {

    @Id
    private String iin;

    @Column
    private String bankUrl;

    public Bank(String iin, String bankUrl) {
        this.iin = iin;
        this.bankUrl = bankUrl;
    }

    public Bank() {

    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getBankUrl() {
        return bankUrl;
    }

    public void setBankUrl(String bankUrl) {
        this.bankUrl = bankUrl;
    }
}
