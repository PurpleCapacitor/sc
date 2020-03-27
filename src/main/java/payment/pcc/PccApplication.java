package payment.pcc;

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
import payment.pcc.repository.BankRepository;

import java.util.Collections;

@SpringBootApplication
public class PccApplication {

    @Autowired
    BankRepository bankRepository;

    public static void main(String[] args) {
        SpringApplication.run(PccApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void testConnection() {
        RestTemplate restTemplate = new RestTemplate();
        /*HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));*/

        //HttpEntity<String> request = new HttpEntity<>("hi", headers);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("https://localhost:8083/erste/test", String.class);
            System.out.println("Connection test passed");
        } catch(RestClientException e) {
            System.out.println(e.getMessage());
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initBanks() {
        if(bankRepository.findAll().isEmpty()) {
            Bank bank = new Bank("432100", "https://localhost:8083/erste/payments");
            bankRepository.save(bank);
        }

    }

}
