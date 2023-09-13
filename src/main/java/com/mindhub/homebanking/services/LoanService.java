package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface LoanService {

    public Set<LoanDTO> getLoans();

    public List<Long> getLoansIds();

    public LoanDTO getLoanById(Long id);

    public ResponseEntity<String> loanApplication(Long loanId, Double amount, Integer payments, String accountNumber, String email);


}
