package kr.megaptera.makaobank.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionTest {
  @Test
  void activity() {
    AccountNumber sender = new AccountNumber("352");
    AccountNumber receiver = new AccountNumber("179");

    Transaction transaction = new Transaction(
        sender, receiver, 2_000L, "황인우"
    );

    assertThat(transaction.activity(sender)).isEqualTo("송금");
    assertThat(transaction.activity(receiver)).isEqualTo("입금");
  }

  @Test
  void name() {
    AccountNumber sender = new AccountNumber("352");
    AccountNumber receiver = new AccountNumber("179");

    Transaction transaction = new Transaction(
        sender, receiver, 2_000L, "황인우"
    );

    assertThat(transaction.name(sender)).isEqualTo("179");
    assertThat(transaction.name(receiver)).isEqualTo("황인우");
  }
}
