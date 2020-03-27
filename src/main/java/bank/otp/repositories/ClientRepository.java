package bank.otp.repositories;

import bank.otp.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByMerchantIdAndMerchantPassword(String merchantId, String password);

}
