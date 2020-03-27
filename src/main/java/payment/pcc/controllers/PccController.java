package payment.pcc.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import payment.pcc.dto.CardDTO;
import payment.pcc.dto.TransactionDTO;
import payment.pcc.repository.BankRepository;
import payment.pcc.service.BankService;

@CrossOrigin(origins = "/*")
@Controller
@RequestMapping(value = "pcc")
public class PccController {

    @Autowired
    BankService bankService;

    @GetMapping(value = "test")
    public ResponseEntity<String> testConnection() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "cards")
    public ResponseEntity<TransactionDTO> routeCardToIssuer(@RequestBody CardDTO cardDTO) throws JsonProcessingException {
        return bankService.routeToBank(cardDTO);
    }
}
