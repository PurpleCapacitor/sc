package bank.erste.model;

import javax.persistence.*;

@Entity
public class Client {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String merchantId;

    @Column
    private String merchantPassword;

   /* @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")*/
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Account account;

    public Client() {
    }

    public Client(Long id, String firstName, String lastName, String merchantId, String merchantPassword, Account account) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.merchantId = merchantId;
        this.merchantPassword = merchantPassword;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantPassword() {
        return merchantPassword;
    }

    public void setMerchantPassword(String merchantPassword) {
        this.merchantPassword = merchantPassword;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
