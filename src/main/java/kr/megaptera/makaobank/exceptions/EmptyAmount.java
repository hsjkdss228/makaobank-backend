package kr.megaptera.makaobank.exceptions;

public class EmptyAmount extends RuntimeException {
  public EmptyAmount() {
    super("Empty amount");
  }
}
