package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AccountServiceTest {
  private AccountRepository accountRepository;
  private AccountService accountService;

  @BeforeEach
  void setUp() {
    accountRepository = mock(AccountRepository.class);

    given(accountRepository.findByAccountNumber("352"))
        .willReturn(Optional.of(Account.fake("352")));

    accountService = new AccountService(accountRepository);
  }

  @Test
  void account() {
    Account account = accountService.detail("352");

    verify(accountRepository).findByAccountNumber("352");
  }
}
