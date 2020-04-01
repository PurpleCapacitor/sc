package bank.erste.service;

import bank.erste.dto.CardDTO;
import bank.erste.dto.TransactionDTO;
import bank.erste.model.Account;
import bank.erste.model.Card;
import bank.erste.model.Transaction;
import bank.erste.model.TransactionStatus;
import bank.erste.repositories.AccountRepository;
import bank.erste.repositories.CardRepository;
import bank.erste.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BankService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    AccountRepository accountRepository;

    public ResponseEntity<TransactionDTO> deductFromAccount(CardDTO cardDTO) {
        Transaction transaction = new Transaction();
        transaction.setAcquirerId(cardDTO.getAcquirerId());
        transaction.setAcquirerTimestamp(LocalDateTime.parse(cardDTO.getAcquirerTimestamp()));
        transaction.setAmount(new BigDecimal(cardDTO.getAmount()));
        transaction.setIssuerOrderId(generateId());
        transaction.setIssuerTimestamp(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.CREATED);
        transactionRepository.save(transaction);

        Card card = null;
        Account account = null;
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

    private TransactionDTO formDto(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAcquirerId(transaction.getAcquirerId().toString());
        transactionDTO.setAcquirerTimestamp(transaction.getAcquirerTimestamp().toString());
        transactionDTO.setIssuerOrderId(transaction.getIssuerOrderId().toString());
        transactionDTO.setIssuerTimestamp(transaction.getIssuerTimestamp().toString());
        transactionDTO.setStatus(transaction.getStatus().toString());
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
