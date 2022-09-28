package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.exceptions.LoginFailed;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.models.AccountNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginServiceTest {
  private LoginService loginService;

  @BeforeEach
  void setUp() {
    loginService = new LoginService();
  }

  @Test
  void loginSuccess() {
    Account account = loginService.login("352", "password");

    assertThat(account.accountNumber()).isEqualTo(new AccountNumber("352"));
  }

  @Test
  void loginFailWithWrongAccountNumber() {
    assertThrows(LoginFailed.class, () -> {
      loginService.login("wrongAccountNumber", "password");
    });
  }

  @Test
  void loginFailWithWrongPassword() {
    assertThrows(LoginFailed.class, () -> {
      loginService.login("352", "wrongPassword");
    });
  }
}
