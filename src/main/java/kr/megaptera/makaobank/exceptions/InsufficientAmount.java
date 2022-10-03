package kr.megaptera.makaobank.exceptions;

public class InsufficientAmount extends RuntimeException {
  public InsufficientAmount(Long amount) {
    super("Insufficient Amount (amount: " + amount + ")");
  }
}
