package kr.megaptera.makaobank.backdoor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/backdoor")
@Transactional
public class BackdoorController {
  private final JdbcTemplate jdbcTemplate;
  private final PasswordEncoder passwordEncoder;

  public BackdoorController(JdbcTemplate jdbcTemplate,
                            PasswordEncoder passwordEncoder) {
    this.jdbcTemplate = jdbcTemplate;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping("/setup-database")
  public String setupDatabase() {
    LocalDateTime now = LocalDateTime.now();

    // 1. 기존 데이터 삭제
    jdbcTemplate.execute("DELETE FROM ACCOUNT");
    jdbcTemplate.execute("DELETE FROM TRANSACTION");

    // 2. 내가 원하는 데이터로 초기화
    jdbcTemplate.update("" +
                  "INSERT INTO ACCOUNT(" +
                  "   ID, ACCOUNT_NUMBER, ENCODED_PASSWORD, " +
                  "   NAME, AMOUNT, CREATED_AT, UPDATED_AT" +
                  ") " +
                  "VALUES(1, ?, ?, ?, ?, ?, ?)",
        "352", passwordEncoder.encode("password"),
        "김인우", 456_000_000, now, now
    );
    jdbcTemplate.update("" +
                  "INSERT INTO ACCOUNT(" +
                  "   ID, ACCOUNT_NUMBER, ENCODED_PASSWORD, " +
                  "   NAME, AMOUNT, CREATED_AT, UPDATED_AT" +
                  ") " +
                  "VALUES(2, ?, ?, ?, ?, ?, ?)",
        "179", passwordEncoder.encode("password"),
        "치코리타", 10, now, now
    );

    return "OK";
  }

  @GetMapping("/change-amount")
  public String changeAmount(
      @RequestParam Long userId,
      @RequestParam Long amount
  ) {
    jdbcTemplate.update("UPDATE ACCOUNT SET AMOUNT=? WHERE ID=?",
        amount, userId);

    return "OK";
  }
}
