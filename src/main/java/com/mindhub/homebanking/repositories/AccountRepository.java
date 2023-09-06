package com.mindhub.homebanking.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.mindhub.homebanking.models.Account;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {
    public Account findByNumber(String number);
    //public Optional<Account> findById(Long id);

}
