package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.exceptions.RegistrationFailed;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.repositories.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(AccountRepository accountRepository,
                     PasswordEncoder passwordEncoder) {
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public Account create(String name, String accountNumber,
                        String password, String confirmPassword) {
    AccountNumber createdAccountNumber = new AccountNumber(accountNumber);

    if (accountRepository.findByAccountNumber(createdAccountNumber).isPresent()) {
      throw new RegistrationFailed("이미 존재하는 계좌번호입니다");
    }

    if (!password.equals(confirmPassword)) {
      throw new RegistrationFailed("비밀번호가 일치하지 않습니다");
    }

    Account account = new Account(name, createdAccountNumber);
    account.changePassword(password, passwordEncoder);
    return accountRepository.save(account);
  }
}
