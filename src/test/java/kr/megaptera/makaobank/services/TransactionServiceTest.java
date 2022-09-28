package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.models.Transaction;
import kr.megaptera.makaobank.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TransactionServiceTest {
  private TransactionRepository transactionRepository;
  private TransactionService transactionService;

  @BeforeEach
  void setUp() {
    transactionRepository = mock(TransactionRepository.class);
    transactionService = new TransactionService(transactionRepository);
  }

  @Test
  void list() {
    AccountNumber accountNumber = new AccountNumber("352");

    Transaction transaction = mock(Transaction.class);

    Sort sort = Sort.by("createdAt").descending();

    given(
        transactionRepository
            .findAllBySenderOrReceiver(
                accountNumber, accountNumber, sort))
        .willReturn(List.of(
            transaction
        ));

    List<Transaction> transactions = transactionService.list(accountNumber);

    assertThat(transactions).hasSize(1);
  }
}
