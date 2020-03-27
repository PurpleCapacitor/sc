package bank.otp.controllers;

import bank.otp.dto.BankResponseDTO;
import bank.otp.dto.CardDTO;
import bank.otp.dto.PaymentRequestDTO;
import bank.otp.dto.TransactionDTO;
import bank.otp.repositories.ClientRepository;
import bank.otp.services.BankService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "/*")
@Controller
@RequestMapping(value = "otpBank")
public class BankController {

    @Autowired
    BankService bankService;

    @Autowired
    ClientRepository clientRepository;


    @PostMapping(value = "payments", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BankResponseDTO> createPaymentIdAndUrl(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        if (clientRepository.findByMerchantIdAndMerchantPassword(paymentRequestDTO.getMerchantId(),
                paymentRequestDTO.getMerchantPassword()) == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        BankResponseDTO bankResponseDTO = bankService.makeBankResponse(paymentRequestDTO);
        return new ResponseEntity<>(bankResponseDTO, HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "cc", consumes = "application/json", produces = "application/json")
    public ResponseEntity<TransactionDTO> validateCC(@RequestBody CardDTO cardDTO) throws JsonProcessingException {
        return bankService.processCCPayment(cardDTO);
    }

    @PostMapping(value = "test", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "test")
    public ResponseEntity<String> testFront() {
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
