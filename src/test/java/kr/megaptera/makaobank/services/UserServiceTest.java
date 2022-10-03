package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.exceptions.RegistrationFailed;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserServiceTest {
  private AccountRepository accountRepository;
  private PasswordEncoder passwordEncoder;
  private UserService userService;

  @BeforeEach
  void setUp() {
    accountRepository = mock(AccountRepository.class);
    passwordEncoder = new Argon2PasswordEncoder();
    userService = new UserService(accountRepository, passwordEncoder);
  }

  @Test
  void create() {
    String name = "황인우";
    AccountNumber accountNumber = new AccountNumber("35205282");

    Account account = new Account(name, accountNumber);
    String password = "Megaptera!1";
    account.changePassword(password, passwordEncoder);

    given(accountRepository.save(any())).willReturn(account);

    Account createdAccount = userService.create(
        name, accountNumber.value(), password, password);

    assertThat(createdAccount).isNotNull();

    verify(accountRepository).save(any());
  }

  @Test
  void alreadyExistingAccountNumber() {
    String name = "황인우";
    AccountNumber accountNumber = new AccountNumber("35205282");

    Account account = new Account(name, accountNumber);
    String password = "Megaptera!1";
    account.changePassword(password, passwordEncoder);

    given(accountRepository.findByAccountNumber(accountNumber))
        .willReturn(Optional.of(account));

    assertThrows(RegistrationFailed.class, () -> {
      userService.create(
          "김인우", accountNumber.value(), "godBlessMe@2", "godBlessMe@2");
    });
  }

  @Test
  void passwordDoNotMatch() {
    String name = "시바견";
    AccountNumber accountNumber = new AccountNumber("35205282");
    String password = "Megaptera!1";
    String confirmPassword = "Ride0nTheWha1e!";

    assertThrows(RegistrationFailed.class, () -> {
      userService.create(
          name, accountNumber.value(), password, confirmPassword);
    });
  }
}
