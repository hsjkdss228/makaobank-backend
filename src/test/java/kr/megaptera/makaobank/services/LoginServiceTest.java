package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.exceptions.LoginFailed;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LoginServiceTest {
  private AccountRepository accountRepository;
  private LoginService loginService;
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    accountRepository = mock(AccountRepository.class);
    passwordEncoder = new Argon2PasswordEncoder();
    loginService = new LoginService(accountRepository, passwordEncoder);
  }

  @Test
  void loginSuccess() {
    AccountNumber accountNumber = new AccountNumber("352");

    Account account = Account.fake(accountNumber.value());
    account.changePassword("password", passwordEncoder);

    given(accountRepository.findByAccountNumber(accountNumber))
        .willReturn(Optional.of(account));

    Account found = loginService.login(accountNumber, "password");

    assertThat(found.accountNumber()).isEqualTo(accountNumber);

    verify(accountRepository).findByAccountNumber(accountNumber);
  }

  @Test
  void loginFailWithWrongAccountNumber() {
    assertThrows(LoginFailed.class, () -> {
      loginService.login(new AccountNumber("wrongAccountNumber"), "password");
    });
  }

  @Test
  void loginFailWithWrongPassword() {
    assertThrows(LoginFailed.class, () -> {
      loginService.login(new AccountNumber("352"), "wrongPassword");
    });
  }
}
