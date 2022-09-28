package kr.megaptera.makaobank.repositories;

import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.models.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TransactionRepositoryTest {
  @Autowired
  private TransactionRepository transactionRepository;

  @Test
  void save() {
    AccountNumber senderAccountNumber = new AccountNumber("352");
    AccountNumber receiverAccountNumber = new AccountNumber("179");
    Long amount = 3_000L;
    String name = "황인우";

    Transaction transaction = new Transaction(
        senderAccountNumber, receiverAccountNumber, amount, name
    );

    transactionRepository.save(transaction);
  }
}
