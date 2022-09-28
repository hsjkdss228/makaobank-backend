package kr.megaptera.makaobank.dtos;

public class AccountDto {
  public String name;

  public String accountNumber;

  public long amount;

  public AccountDto(String name, String accountNumber, long amount) {
    this.name = name;
    this.accountNumber = accountNumber;
    this.amount = amount;
  }

  public String getName() {
    return name;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public long getAmount() {
    return amount;
  }
}
