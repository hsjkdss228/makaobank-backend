package kr.megaptera.makaobank.models;

import kr.megaptera.makaobank.dtos.AccountDto;
import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

  private String accountNumber;

  private Long amount;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public Account() {

  }

  public Account(String name, String accountNumber) {
    this.name = name;
    this.accountNumber = accountNumber;
    this.amount = 0L;
  }

  public Account(Long id, String name, String accountNumber, Long amount) {
    this.id = id;
    this.name = name;
    this.accountNumber = accountNumber;
    this.amount = amount;
  }

  public void transferTo(Account other, Long amount) {
    if (amount <= 0 || amount > this.amount) {
      throw new IncorrectAmount(amount);
    }

    this.amount -= amount;
    other.amount += amount;
  }

  public String accountNumber() {
    return accountNumber;
  }

  public Long amount() {
    return amount;
  }

  public AccountDto toDto() {
    return new AccountDto(name, accountNumber, amount);
  }

  public static Account fake(String accountNumber) {
    return new Account(1L, "황인우", accountNumber, 1_000_000L);
  }
}
