package bank.erste.controllers;

import bank.erste.dto.CardDTO;
import bank.erste.dto.TransactionDTO;
import bank.erste.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "/*")
@Controller
@RequestMapping(value = "erste")
public class BankController {

    @Autowired
    BankService bankService;

    @GetMapping(value = "test")
    public ResponseEntity<String> testConnection() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "payments", consumes = "application/json", produces = "application/json")
    public ResponseEntity<TransactionDTO> paymentProcessing(@RequestBody CardDTO cardDTO) {
        return bankService.deductFromAccount(cardDTO);
    }
}
