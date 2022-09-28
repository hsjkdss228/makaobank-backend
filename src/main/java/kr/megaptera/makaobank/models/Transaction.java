package kr.megaptera.makaobank.models;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Transaction {
  @Id
  @GeneratedValue
  private Long id;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "sender"))
  private AccountNumber senderAccountNumber;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "receiver"))
  private AccountNumber receiverAccountNumber;

  private Long amount;

  private String name;

  public Transaction() {

  }

  public Transaction(AccountNumber senderAccountNumber,
                     AccountNumber receiverAccountNumber,
                     Long amount, String name) {
    this.senderAccountNumber = senderAccountNumber;
    this.receiverAccountNumber = receiverAccountNumber;
    this.amount = amount;
    this.name = name;
  }
}
