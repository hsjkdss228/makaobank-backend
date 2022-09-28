package kr.megaptera.makaobank.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountNumberTest {
  @Test
  void equals() {
    assertThat(new AccountNumber("352")).isEqualTo(new AccountNumber("352"));
    assertThat(new AccountNumber("352")).isNotEqualTo(new AccountNumber("179"));
    assertThat(new AccountNumber("352")).isNotEqualTo(null);
    assertThat(new AccountNumber("352")).isNotEqualTo("352");
  }
}
