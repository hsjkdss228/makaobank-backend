package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.models.Transaction;
import kr.megaptera.makaobank.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    Transaction transaction = mock(Transaction.class);

    given(transactionRepository.findAll())
        .willReturn(List.of(
           transaction
        ));

    List<Transaction> transactions = transactionService.list();

    assertThat(transactions).hasSize(1);
  }
}