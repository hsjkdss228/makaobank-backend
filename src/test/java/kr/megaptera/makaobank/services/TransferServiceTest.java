package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.exceptions.AccountNotFound;
import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import kr.megaptera.makaobank.exceptions.TransferToMyAccount;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.repositories.AccountRepository;
import kr.megaptera.makaobank.repositories.TransactionRepository;
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
  private TransactionRepository transactionRepository;
  private TransferService transferService;

  @BeforeEach
  void setUp() {
    accountRepository = mock(AccountRepository.class);
    transactionRepository = mock(TransactionRepository.class);
    transferService
        = new TransferService(accountRepository, transactionRepository);
  }

  @Test
  void transferTo() {
    Long senderAmount = 1_000_000L;
    Long receiverAmount = 10L;
    Long transferAmount = 3_000L;

    AccountNumber accountNumber1 = new AccountNumber("352");
    AccountNumber accountNumber2 = new AccountNumber("179");

    Account account1 = spy(new Account(1L, "황인우", accountNumber1, senderAmount));
    Account account2 = new Account(2L, "치코리타", accountNumber2, receiverAmount);

    given(accountRepository.findByAccountNumber(account1.accountNumber()))
        .willReturn(Optional.of(account1));
    given(accountRepository.findByAccountNumber(account2.accountNumber()))
        .willReturn(Optional.of(account2));

    String name = "김인우";

    transferService.transfer(accountNumber1, accountNumber2, transferAmount, name);

    verify(account1).transferTo(account2, transferAmount);

    verify(transactionRepository).save(any());
  }

  @Test
  void transferWithIncorrectFromAccountNumber() {
    AccountNumber wrongAccountNumber = new AccountNumber("6666666");

    Long receiverAmount = 10L;
    Long transferAmount = 3_000L;

    AccountNumber accountNumber = new AccountNumber("179");

    Account account = new Account(2L, "치코리타", accountNumber, receiverAmount);

    given(accountRepository.findByAccountNumber(account.accountNumber()))
        .willReturn(Optional.of(account));

    String name = "???";

    assertThrows(AccountNotFound.class, () -> {
      transferService.transfer(
          wrongAccountNumber, account.accountNumber(), transferAmount, name);
    });
  }

  @Test
  void transferWithIncorrectToAccountNumber() {
    AccountNumber wrongAccountNumber = new AccountNumber("6666666");

    Long senderAmount = 1_000_000L;
    Long transferAmount = 3_000L;

    AccountNumber accountNumber = new AccountNumber("352");

    Account account = new Account(1L, "황인우", accountNumber, senderAmount);

    given(accountRepository.findByAccountNumber(account.accountNumber()))
        .willReturn(Optional.of(account));

    String name = "김인우";

    assertThrows(AccountNotFound.class, () -> {
      transferService.transfer(
          account.accountNumber(), wrongAccountNumber, transferAmount, name);
    });
  }

  @Test
  void transferToMyAccountNumber() {
    AccountNumber myAccountNumber = new AccountNumber("352");

    Long senderAmount = 1_000_000L;
    Long transferAmount = 3_000L;

    AccountNumber accountNumber = new AccountNumber("352");

    Account account = new Account(1L, "황인우", accountNumber, senderAmount);

    given(accountRepository.findByAccountNumber(account.accountNumber()))
        .willReturn(Optional.of(account));

    String name = "김인우";

    assertThrows(TransferToMyAccount.class, () -> {
      transferService.transfer(
          account.accountNumber(), myAccountNumber, transferAmount, name);
    });
  }
}
