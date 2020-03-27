package bank.erste.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {

    @Id
    private Long id;

    @Column
    private String pan;

    @Column
    private Integer cvv;

    @Column
    private String cardHolderName;

    @Column
    private LocalDate validThru;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Account account;

    public Card() {

    }

    public Card(Long id, String pan, Integer cvv, String cardHolderName, LocalDate validThru, Account account) {
        this.id = id;
        this.pan = pan;
        this.cvv = cvv;
        this.cardHolderName = cardHolderName;
        this.validThru = validThru;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public LocalDate getValidThru() {
        return validThru;
    }

    public void setValidThru(LocalDate validThru) {
        this.validThru = validThru;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
