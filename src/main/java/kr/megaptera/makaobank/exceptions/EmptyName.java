package kr.megaptera.makaobank.exceptions;

public class EmptyName extends RuntimeException {
  public EmptyName() {
    super("Empty name");
  }
}
