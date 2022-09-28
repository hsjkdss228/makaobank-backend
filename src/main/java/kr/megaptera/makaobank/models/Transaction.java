package kr.megaptera.makaobank.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Transaction {
  @Id
  @GeneratedValue
  private Long id;

  // User Id
  private Long senderId;

  // User Id
  private Long receiverId;

  private Long amount;

  private String name;
}
