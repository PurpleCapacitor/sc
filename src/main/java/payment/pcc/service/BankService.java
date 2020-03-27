package payment.pcc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import payment.pcc.Bank;
import payment.pcc.dto.CardDTO;
import payment.pcc.dto.TransactionDTO;
import payment.pcc.repository.BankRepository;


import java.util.Collections;
import java.util.List;

@Service
public class BankService {

    @Autowired
    BankRepository bankRepository;

    public ResponseEntity<TransactionDTO> routeToBank(CardDTO cardDTO) throws JsonProcessingException {

        List<Bank> banks = bankRepository.findAll();
        String bankUrl;
        for (Bank bank : banks) {
            if (cardDTO.getPan().startsWith(bank.getIin())) {
                bankUrl = bank.getBankUrl();
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

                HttpEntity<CardDTO> cardData = new HttpEntity<>(cardDTO, headers);
                try {
                    return restTemplate.postForEntity(bankUrl, cardData, TransactionDTO.class);
                } catch (RestClientResponseException e) {
                    System.out.println(e.getMessage());
                    TransactionDTO transactionDTO = new ObjectMapper().readValue(e.getResponseBodyAsString(), TransactionDTO.class);
                    return new ResponseEntity<>(transactionDTO, HttpStatus.FORBIDDEN);
                }

            } else {
                System.out.println("No bank with IIN provided");
                TransactionDTO transactionDTO = new TransactionDTO();
                transactionDTO.setAcquirerId(Long.toString(cardDTO.getAcquirerId()));
                transactionDTO.setAcquirerTimestamp(cardDTO.getAcquirerTimestamp());
                transactionDTO.setIssuerOrderId("");
                transactionDTO.setIssuerTimestamp("");
                transactionDTO.setStatus("FAILED");
                return new ResponseEntity<>(transactionDTO, HttpStatus.FORBIDDEN);
            }
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
