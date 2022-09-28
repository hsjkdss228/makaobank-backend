package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.exceptions.LoginFailed;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.repositories.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class LoginService {
  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;

  public LoginService(AccountRepository accountRepository,
                      PasswordEncoder passwordEncoder) {
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public Account login(AccountNumber accountNumber, String password) {
    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new LoginFailed());

    // TODO. Password를 비교한다.
    if (!account.authenticate(password, passwordEncoder)) {
      throw new LoginFailed();
    }

    return account;
  }
}
