package kr.megaptera.makaobank.utils;

import kr.megaptera.makaobank.models.AccountNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {
  private static final String SECRET = "SECRET";

  private JwtUtil jwtUtil;

  @BeforeEach
  void setUp() {
    jwtUtil = new JwtUtil(SECRET);
  }

  @Test
  void encodeAndDecode() {
    AccountNumber original = new AccountNumber("352");
    String token = jwtUtil.encode(original);

    assertThat(token).contains(".");

    AccountNumber accountNumber = jwtUtil.decode(token);
    assertThat(accountNumber).isEqualTo(original);
  }
}
