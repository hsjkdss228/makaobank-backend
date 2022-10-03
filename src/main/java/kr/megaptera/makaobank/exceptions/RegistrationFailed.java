package kr.megaptera.makaobank.exceptions;

import java.util.List;

public class RegistrationFailed extends RuntimeException {
  private final List<String> errorMessages;

  public RegistrationFailed(String message) {
    errorMessages = List.of(message);
  }

  public RegistrationFailed(List<String> errorMessages) {
    this.errorMessages = errorMessages.stream().toList();
  }

  public List<String> errorMessages() {
    return this.errorMessages.stream().toList();
  }
}
