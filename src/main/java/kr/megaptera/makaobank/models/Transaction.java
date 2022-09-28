package kr.megaptera.makaobank.models;

import kr.megaptera.makaobank.dtos.TransactionDto;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Transaction {
  @Id
  @GeneratedValue
  private Long id;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "sender"))
  private AccountNumber sender;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "receiver"))
  private AccountNumber receiver;

  private Long amount;

  private String name;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public Transaction() {

  }

  public Transaction(AccountNumber senderAccountNumber,
                     AccountNumber receiverAccountNumber,
                     Long amount, String name) {
    this.sender = senderAccountNumber;
    this.receiver = receiverAccountNumber;
    this.amount = amount;
    this.name = name;
  }

  public TransactionDto toDto(AccountNumber currentAccountNumber) {
    return new TransactionDto(
        id,
        activity(currentAccountNumber),
        name(currentAccountNumber),
        amount
    );
  }

  public String activity(AccountNumber currentAccountNumber) {
    return currentAccountNumber.equals(sender)
        ? "송금"
        : "입금";
  }

  public String name(AccountNumber currentAccountNumber) {
    return currentAccountNumber.equals(sender)
        ? receiver.value()
        : name;
  }
}
