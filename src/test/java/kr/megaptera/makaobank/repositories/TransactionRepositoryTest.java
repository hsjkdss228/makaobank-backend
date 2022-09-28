package kr.megaptera.makaobank.repositories;

import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.models.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TransactionRepositoryTest {
  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

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

  @Test
  void findAllBySenderOrReceiver() {
    AccountNumber senderAccountNumber = new AccountNumber("352");
    AccountNumber receiverAccountNumber = new AccountNumber("179");
    Long amount = 3_000L;
    String name = "황인우";
    LocalDateTime now = LocalDateTime.now();

    jdbcTemplate.execute("DELETE FROM TRANSACTION");

    jdbcTemplate.update("" +
            "INSERT INTO TRANSACTION(" +
            "   ID, SENDER, RECEIVER, AMOUNT, NAME," +
            "   CREATED_AT, UPDATED_AT" +
            ") " +
            "VALUES(1, ?, ?, ?, ?, ?, ?)",
        senderAccountNumber.value(),
        receiverAccountNumber.value(),
        amount, name, now, now
    );

    Sort sort = Sort.by("id").descending();
    List<Transaction> transactions
        = transactionRepository.findAllBySenderOrReceiver(
        senderAccountNumber, senderAccountNumber, sort);

    assertThat(transactions).hasSize(1);
    assertThat(transactions.get(0).activity(senderAccountNumber))
        .isEqualTo("송금");
  }
}
