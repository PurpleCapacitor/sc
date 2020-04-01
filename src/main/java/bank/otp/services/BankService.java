package bank.otp.services;

import bank.otp.dto.*;
import bank.otp.model.Account;
import bank.otp.model.Card;
import bank.otp.model.Transaction;
import bank.otp.model.TransactionStatus;
import bank.otp.repositories.AccountRepository;
import bank.otp.repositories.CardRepository;
import bank.otp.repositories.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BankService {

    @Autowired
    TransactionRepository transactionRepository;

    @Value("${bank.iin}")
    private String bankIIN;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    AccountRepository accountRepository;

    private static final String pccUrl = "https://localhost:8082/pcc/cards";

    public BankResponseDTO makeBankResponse(PaymentRequestDTO paymentRequestDTO) {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(paymentRequestDTO.getAmount()));
        transaction.setMerchantId(paymentRequestDTO.getMerchantId());
        transaction.setMerchantOrderId(paymentRequestDTO.getMerchantOrderId());
        transaction.setStatus(TransactionStatus.CREATED);
        transaction.setTimestamp(LocalDateTime.parse(paymentRequestDTO.getTimestamp()));
        transaction.setSuccessUrl(paymentRequestDTO.getSuccessUrl());
        transaction.setErrorUrl(paymentRequestDTO.getErrorUrl());
        transaction.setFailedUrl(paymentRequestDTO.getFailedUrl());
        transaction.setId(generateId()); //paymentId

        transactionRepository.save(transaction);

        BankResponseDTO bankResponseDTO = new BankResponseDTO();
        bankResponseDTO.setPaymentUrl("/otpBank/".concat(UUID.randomUUID().toString()));
        bankResponseDTO.setPaymentId(transaction.getId().toString());

        return bankResponseDTO;
    }


    public ResponseEntity<TransactionDTO> processCCPayment(CardDTO cardDTO) throws JsonProcessingException {
        if (!checkCardIssuer(cardDTO.getPan())) {
            Transaction transaction = transactionRepository.findById(Long.valueOf(cardDTO.getPaymentId())).get();
            transaction.setAcquirerId(generateId());
            transaction.setAcquirerTimestamp(LocalDateTime.now());
            transactionRepository.save(transaction);

            cardDTO.setAcquirerId(transaction.getAcquirerId());
            cardDTO.setAcquirerTimestamp(transaction.getAcquirerTimestamp().toString());
            cardDTO.setAmount(transaction.getAmount().toString());

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<CardDTO> cardData = new HttpEntity<>(cardDTO, headers);

            try {
                ResponseEntity<PccDTO> response = restTemplate.postForEntity(pccUrl, cardData, PccDTO.class);
                System.out.println("Pcc contacted");
                transaction.setStatus(TransactionStatus.valueOf(response.getBody().getStatus()));
                transactionRepository.save(transaction);
                TransactionDTO transactionDTO = formDto(transaction);
                return new ResponseEntity<>(transactionDTO, HttpStatus.OK);
            } catch(RestClientResponseException e) {
                System.out.println(e.getMessage());
                PccDTO pccDTO = new ObjectMapper().readValue(e.getResponseBodyAsString(), PccDTO.class);
                transaction.setStatus(TransactionStatus.valueOf(pccDTO.getStatus()));
                transactionRepository.save(transaction);
                TransactionDTO transactionDTO = formDto(transaction);
                return new ResponseEntity<>(transactionDTO, HttpStatus.FORBIDDEN);
            }

        }
        Card card = null;
        Account account = null;
        Transaction transaction = transactionRepository.findById(Long.valueOf(cardDTO.getPaymentId())).get();
        try {
            card = cardRepository.findByPanAndCardHolderNameAndCvv(cardDTO.getPan(),
                    cardDTO.getCardHolderName(), cardDTO.getCcv());
            account = card.getAccount();
        } catch (NullPointerException e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            TransactionDTO transactionDTO = formDto(transaction);
            return new ResponseEntity<>(transactionDTO, HttpStatus.PRECONDITION_FAILED);
        }

        if (!checkCardValidThru(card.getValidThru())) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            TransactionDTO transactionDTO = formDto(transaction);
            return new ResponseEntity<>(transactionDTO, HttpStatus.PRECONDITION_FAILED);
        }

        if (account.getAmount().compareTo(transaction.getAmount()) == -1) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            TransactionDTO transactionDTO = formDto(transaction);
            return new ResponseEntity<>(transactionDTO, HttpStatus.PRECONDITION_FAILED);
        }

        BigDecimal newAccountBalance = account.getAmount().subtract(transaction.getAmount());
        account.setAmount(newAccountBalance);
        accountRepository.save(account);

        transaction.setStatus(TransactionStatus.SUCCESS);
        transactionRepository.save(transaction);
        TransactionDTO transactionDTO = formDto(transaction);
        return new ResponseEntity<>(transactionDTO, HttpStatus.OK);
    }

    private boolean checkCardIssuer(String pan) {
        return pan.startsWith(bankIIN);
    }

    private TransactionDTO formDto(Transaction transaction) {

        if(transaction.getStatus().equals(TransactionStatus.FAILED)) {
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setMerchantOrderId(transaction.getMerchantOrderId());
            transactionDTO.setPaymentId(transaction.getId());
            transactionDTO.setStatus(transaction.getStatus().toString());
            transactionDTO.setUrl(transaction.getFailedUrl());
            return transactionDTO;
        } else if(transaction.getStatus().equals(TransactionStatus.ERROR)) {
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setMerchantOrderId(transaction.getMerchantOrderId());
            transactionDTO.setPaymentId(transaction.getId());
            transactionDTO.setStatus(transaction.getStatus().toString());
            transactionDTO.setUrl(transaction.getErrorUrl());
            return transactionDTO;
        }

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setMerchantOrderId(transaction.getMerchantOrderId());
        transactionDTO.setPaymentId(transaction.getId());
        transactionDTO.setStatus(transaction.getStatus().toString());
        transactionDTO.setUrl(transaction.getSuccessUrl());
        return transactionDTO;

    }

    private boolean checkCardValidThru(LocalDate validThru) {
        LocalDate dateNow = LocalDate.now();
        return dateNow.isBefore(validThru) || dateNow.isEqual(validThru);

    }

    public long generateId() { // 10 digit id
        return ThreadLocalRandom.current().nextLong(1000000000, 9999999999L);
    }
}
