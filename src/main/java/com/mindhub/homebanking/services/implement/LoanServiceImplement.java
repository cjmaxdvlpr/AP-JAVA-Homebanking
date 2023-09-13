package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoanServiceImplement implements LoanService {

    @Autowired
    private LoanRepository loanRepo;

    @Autowired
    private ClientLoanRepository clientLoanRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private ClientRepository clientRepo;

    @Override
    public Set<LoanDTO> getLoans(){
        return loanRepo.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toSet());
    }

    @Override
    public List<Long> getLoansIds(){
        return loanRepo.findAll().stream().map(loan -> new LoanDTO(loan).getId()).collect(Collectors.toList());
    }

    @Override
    public LoanDTO getLoanById(Long id){
        if(loanRepo.findById(id).orElse(null) != null) {
            return new LoanDTO(loanRepo.findById(id).orElse(null));
        }else{
            return null;
        }
    }

    @Override
    public ResponseEntity<String> loanApplication(Long loanId, Double amount, Integer payments, String accountNumber, String email){

        Loan loan = loanRepo.findById(loanId).orElse(null);
        Account account = accountRepo.findByNumber(accountNumber);
        Client client = clientRepo.findByEmail(email);


        ClientLoan clientLoan = new ClientLoan(amount + 0.2 * amount, payments, client, loan);
        clientLoanRepo.save(clientLoan);
        Transaction transaction = new Transaction(TransactionType.CREDIT, amount, loan.getName() + " - Loan approved");
        transactionRepo.save(transaction);
        account.addTransaction(transaction);
        account.setBalance(account.getBalance() + amount);
        accountRepo.save(account);

        return new ResponseEntity<>("Accredited loan", HttpStatus.CREATED);
    }
}
