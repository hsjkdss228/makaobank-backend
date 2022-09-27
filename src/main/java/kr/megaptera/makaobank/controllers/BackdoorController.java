package kr.megaptera.makaobank.controllers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backdoor")
@Transactional
public class BackdoorController {
  private final JdbcTemplate jdbcTemplate;

  public BackdoorController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @GetMapping("/setup-database")
  public String setupDatabase() {
    // 1. 기존 데이터 삭제
    jdbcTemplate.execute("DELETE FROM ACCOUNT");

    // 2. 내가 원하는 데이터로 초기화
    jdbcTemplate.execute("" +
        "INSERT INTO " +
        "ACCOUNT(ID, NAME, ACCOUNT_NUMBER, AMOUNT) " +
        "VALUES(1, '김인우', '352', 300)");
    jdbcTemplate.execute("" +
        "INSERT INTO " +
        "ACCOUNT(ID, NAME, ACCOUNT_NUMBER, AMOUNT) " +
        "VALUES(2, '치코리타', '179', 10)");

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
