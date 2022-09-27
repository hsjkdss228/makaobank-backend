package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.exceptions.AccountNotFound;
import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class TransferServiceTest {
  private AccountRepository accountRepository;
  private TransferService transferService;

  @BeforeEach
  void setUp() {
    accountRepository = mock(AccountRepository.class);
    transferService = new TransferService(accountRepository);
  }

  @Test
  void transferTo() {
    Long senderAmount = 1_000_000L;
    Long receiverAmount = 10L;
    Long transferAmount = 3_000L;

    Account account1 = spy(new Account(1L, "FROM", "352", senderAmount));
    Account account2 = new Account(2L, "TO", "179", receiverAmount);

    given(accountRepository.findByAccountNumber(account1.accountNumber()))
        .willReturn(Optional.of(account1));
    given(accountRepository.findByAccountNumber(account2.accountNumber()))
        .willReturn(Optional.of(account2));

    transferService.transfer("352", "179", transferAmount);

    verify(account1).transferTo(account2, transferAmount);
  }

  @Test
  void transferWithIncorrectFromAccountNumber() {
    String wrongAccountNumber = "6666666";

    Long receiverAmount = 10L;
    Long transferAmount = 3_000L;

    Account account = new Account(2L, "TO", "179", receiverAmount);

    given(accountRepository.findByAccountNumber(account.accountNumber()))
        .willReturn(Optional.of(account));

    assertThrows(AccountNotFound.class, () -> {
      transferService.transfer(
          wrongAccountNumber, account.accountNumber(), transferAmount);
    });
  }

  @Test
  void transferWithIncorrectToAccountNumber() {
    String wrongAccountNumber = "6666666";

    Long senderAmount = 1_000_000L;
    Long transferAmount = 3_000L;

    Account account = new Account(1L, "FROM", "352", senderAmount);

    given(accountRepository.findByAccountNumber(account.accountNumber()))
        .willReturn(Optional.of(account));

    assertThrows(AccountNotFound.class, () -> {
      transferService.transfer(
          account.accountNumber(), wrongAccountNumber, transferAmount);
    });
  }
}
