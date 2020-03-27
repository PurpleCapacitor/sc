package bank.erste.repositories;

import bank.erste.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Card findByPanAndCardHolderNameAndCvv(String pan, String cardHolderName, Integer ccv);
}
