package com.mindhub.homebanking.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.mindhub.homebanking.models.Account;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {

}
