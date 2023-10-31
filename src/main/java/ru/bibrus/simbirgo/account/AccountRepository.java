package ru.bibrus.simbirgo.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> getAccountByUsername(String username);

    Optional<Account> findByUsername(String username);

    boolean existsAccountByUsername(String username);

}