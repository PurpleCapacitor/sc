package bank.otp;

import bank.otp.model.Account;
import bank.otp.model.Card;
import bank.otp.model.Client;
import bank.otp.repositories.AccountRepository;
import bank.otp.repositories.CardRepository;
import bank.otp.repositories.ClientRepository;
import bank.otp.services.BankService;
import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class OtpApplication {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    BankService bankService;

    public static void main(String[] args) {
        SpringApplication.run(OtpApplication.class, args);

    }

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<String>("hi", headers);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("https://localhost:8082/pcc/test", String.class);
            System.out.println("Connection test passed");
        } catch(RestClientException e) {
            System.out.println(e.getMessage());
        }


        if(accountRepository.findAll().isEmpty()) {
            Account merchantAcc1 = new Account();
            merchantAcc1.setAmount(new BigDecimal("51.1"));
            Account merchantAcc2 = new Account();
            merchantAcc2.setAmount(new BigDecimal("98.81"));
            Account buyerAcc1 = new Account();
            buyerAcc1.setAmount(new BigDecimal("100"));
            Account buyerAcc2 = new Account();
            buyerAcc2.setAmount(new BigDecimal("195.24"));

            accountRepository.save(merchantAcc1);
            accountRepository.save(merchantAcc2);
            accountRepository.save(buyerAcc1);
            accountRepository.save(buyerAcc2);

            Client merchant1 = new Client(merchantAcc1.getId(), "", "",
                    "magazine1", "pass", merchantAcc1);
            Client merchant2 = new Client(merchantAcc2.getId(), "", "",
                    "magazine2", "pass", merchantAcc2);

            Client buyer1 = new Client(buyerAcc1.getId(), "Mike", "Bike",
                    "", "", buyerAcc1);
            Client buyer2 = new Client(buyerAcc2.getId(), "John", "Doe",
                    "", "", buyerAcc2);

            clientRepository.save(merchant1);
            clientRepository.save(merchant2);
            clientRepository.save(buyer1);
            clientRepository.save(buyer2);

            Card card1 = new Card(buyerAcc1.getId(), "4123451234560123", 123, "Mike Bike",
                    LocalDate.now().plusYears(2), buyerAcc1);
            /*Card card2 = new Card(buyerAcc2.getId(), "4321009876543210", 987, "John Doe",
                    LocalDate.now().plusYears(2), buyerAcc2);*/
            cardRepository.save(card1);
            //cardRepository.save(card2);
        }



    }

}
