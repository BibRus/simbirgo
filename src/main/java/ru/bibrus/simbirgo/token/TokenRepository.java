package ru.bibrus.simbirgo.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bibrus.simbirgo.account.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findAllValidTokenByAccount(Account account);

    Optional<Token> findByToken(String token);

}