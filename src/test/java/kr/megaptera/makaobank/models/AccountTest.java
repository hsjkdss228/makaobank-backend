package kr.megaptera.makaobank.models;

import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import kr.megaptera.makaobank.exceptions.InsufficientAmount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {
  private final Long SENDER_AMOUNT = 1_000_000L;
  private final Long RECEIVER_AMOUNT = 10L;

  private Account account1;
  private Account account2;

  @BeforeEach
  void setUp() {
    account1 = new Account(1L, "FROM", new AccountNumber("352"), SENDER_AMOUNT);
    account2 = new Account(2L, "To", new AccountNumber("179"), RECEIVER_AMOUNT);
  }

  @Test
  void transferTo() {
    Long transferAmount = 3_000L;

    account1.transferTo(account2, transferAmount);

    assertThat(account1.amount()).isEqualTo(SENDER_AMOUNT - transferAmount);
    assertThat(account2.amount()).isEqualTo(RECEIVER_AMOUNT + transferAmount);
  }

  @Test
  void transferWithNegativeAmount() {
    Long negativeTransferAmount = -3_000L;

    assertThrows(IncorrectAmount.class, () -> {
      account1.transferTo(account2, negativeTransferAmount);
    });
  }

  @Test
  void transferWithTooLargeAmount() {
    Long tooLargeTransferAmount = 500_000_000L;

    assertThrows(InsufficientAmount.class, () -> {
      account1.transferTo(account2, tooLargeTransferAmount);
    });
  }
}
