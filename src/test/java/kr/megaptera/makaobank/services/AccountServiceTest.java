package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.models.AccountNumber;
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

  private AccountNumber accountNumber;

  @BeforeEach
  void setUp() {
    accountRepository = mock(AccountRepository.class);

    accountNumber = new AccountNumber("352");

    given(accountRepository.findByAccountNumber(accountNumber))
        .willReturn(
            Optional.of(Account.fake(accountNumber.value())));

    accountService = new AccountService(accountRepository);
  }

  @Test
  void account() {
    Account account = accountService.detail(accountNumber);

    verify(accountRepository).findByAccountNumber(accountNumber);
  }
}
