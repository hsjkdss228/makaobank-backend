package kr.megaptera.makaobank.models;

import kr.megaptera.makaobank.dtos.AccountDto;
import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import kr.megaptera.makaobank.exceptions.InsufficientAmount;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Account {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @Embedded
  private AccountNumber accountNumber;

  private String encodedPassword;

  private Long amount;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public Account() {

  }

  public Account(String name, AccountNumber accountNumber) {
    this.name = name;
    this.accountNumber = accountNumber;
    this.amount = 0L;
  }

  public Account(Long id, String name, AccountNumber accountNumber, Long amount) {
    this.id = id;
    this.name = name;
    this.accountNumber = accountNumber;
    this.amount = amount;
  }

  public void transferTo(Account other, Long amount) {
    if (amount <= 0) {
      throw new IncorrectAmount(amount);
    }

    if (amount > this.amount) {
      throw new InsufficientAmount(amount);
    }

    this.amount -= amount;
    other.amount += amount;
  }

  public boolean authenticate(String password,
                              PasswordEncoder passwordEncoder) {
    return passwordEncoder.matches(password, encodedPassword);
  }

  public void changePassword(String password,
                             PasswordEncoder passwordEncoder) {
    encodedPassword = passwordEncoder.encode(password);
  }

  public AccountNumber accountNumber() {
    return accountNumber;
  }

  public Long amount() {
    return amount;
  }

  public String name() {
    return name;
  }

  public AccountDto toDto() {
    return new AccountDto(name, accountNumber.value(), amount);
  }

  public static Account fake(String accountNumber) {
    return new Account(
        1L, "황인우", new AccountNumber(accountNumber), 1_000_000L);
  }
}
