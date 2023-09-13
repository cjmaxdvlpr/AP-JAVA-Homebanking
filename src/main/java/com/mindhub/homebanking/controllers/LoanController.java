package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @RequestMapping("/loans")
    public Set<LoanDTO> getLoans(){
        return loanService.getLoans();
    }

    @Transactional
    @RequestMapping(value="/loans", method= RequestMethod.POST)
    public ResponseEntity<String> addLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {
    //We are receiving data from the front in an object (LoanApplicatiopnDTO type)

        if (loanApplicationDTO.getLoanId().toString().isEmpty() ||
            loanApplicationDTO.getToAccountNumber().isEmpty() ||
            loanApplicationDTO.getAmount().toString().isEmpty() ||
            loanApplicationDTO.getPayments().toString().isEmpty()){

            String message = (loanApplicationDTO.getLoanId().toString().isEmpty()? "Loan id. ":"") +
                             (loanApplicationDTO.getToAccountNumber().isEmpty()?"Account number. ":"") +
                             (loanApplicationDTO.getAmount().toString().isEmpty()?"Amount. ":"") +
                             (loanApplicationDTO.getPayments().toString().isEmpty()?"Payments. ":"");
            return new ResponseEntity<>("Missing data: " + message, HttpStatus.FORBIDDEN);
        }

        LoanDTO loanDTO = loanService.getLoanById(loanApplicationDTO.getLoanId());
        AccountDTO accountDTO = accountService.getAccountByNumber(loanApplicationDTO.getToAccountNumber());
        ClientDTO clientDTO = clientService.getClientByEmail(authentication.getName());

        if(loanApplicationDTO.getAmount() == 0){
            return new ResponseEntity<>("Credit amount can not be zero", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getPayments() == 0){
            return new ResponseEntity<>("Credit payments can not be zero", HttpStatus.FORBIDDEN);
        }

        if (loanDTO == null){
            return new ResponseEntity<>("Loan does not exist", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > loanDTO.getMaxAmount()){
            return new ResponseEntity<>("Requested amount exceeds maximum loan amount", HttpStatus.FORBIDDEN);
        }
        if (!loanDTO.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("Payments quantity not allowed", HttpStatus.FORBIDDEN);
        }
        if (accountDTO == null){
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }
        if (!clientService.getClientAccountsIds(authentication.getName())
                .contains(accountService.getAccountByNumber(loanApplicationDTO.getToAccountNumber()).getId())){
            return new ResponseEntity<>("Destination account does not belong to client", HttpStatus.FORBIDDEN);
        }

        return loanService.loanApplication(loanApplicationDTO.getLoanId(),
                                    loanApplicationDTO.getAmount(),
                                    loanApplicationDTO.getPayments(),
                                    loanApplicationDTO.getToAccountNumber(),
                                    authentication.getName());


    }

}
