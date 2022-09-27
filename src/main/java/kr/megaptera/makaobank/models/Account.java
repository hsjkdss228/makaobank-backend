package kr.megaptera.makaobank.models;

import kr.megaptera.makaobank.dtos.AccountDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Account {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String accountNumber;

  private long amount;

  public Account() {

  }

  public Account(Long id, String name, String accountNumber, long amount) {
    this.id = id;
    this.name = name;
    this.accountNumber = accountNumber;
    this.amount = amount;
  }

  public AccountDto toDto() {
    return new AccountDto(name, accountNumber, amount);
  }

  public static Account fake(String accountNumber) {
    return new Account(1L, "황인우", accountNumber, 1_000_000L);
  }
}
