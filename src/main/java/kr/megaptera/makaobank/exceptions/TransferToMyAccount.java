package kr.megaptera.makaobank.exceptions;

public class TransferToMyAccount extends RuntimeException {
  public TransferToMyAccount() {
    super("Transfer to my account");
  }
}
