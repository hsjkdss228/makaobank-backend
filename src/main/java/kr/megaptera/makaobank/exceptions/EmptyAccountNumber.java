package kr.megaptera.makaobank.exceptions;

public class EmptyAccountNumber extends RuntimeException {
  public EmptyAccountNumber() {
    super("Empty account number");
  }
}
