package bank.erste;

import bank.erste.model.Account;
import bank.erste.model.Card;
import bank.erste.model.Client;
import bank.erste.repositories.AccountRepository;
import bank.erste.repositories.CardRepository;
import bank.erste.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class ErsteApplication {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;

    public static void main(String[] args) {
        SpringApplication.run(ErsteApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        if (accountRepository.findAll().isEmpty()) {

            Account buyerAcc1 = new Account();
            buyerAcc1.setAmount(new BigDecimal("100"));
            accountRepository.save(buyerAcc1);

            Client buyer1 = new Client(buyerAcc1.getId(), "Max", "Pax",
                    "", "", buyerAcc1);
            clientRepository.save(buyer1);

            Card card1 = new Card(buyerAcc1.getId(), "4321009876543210", 987, "Max Pax",
                    LocalDate.now().plusYears(2), buyerAcc1);
            cardRepository.save(card1);
        }
    }

}
