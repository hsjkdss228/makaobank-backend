package kr.megaptera.makaobank.dtos;

import java.util.Map;

public class RegistrationFailedErrorDto {
  private final Map<Integer, String> codesAndMessages;

  public RegistrationFailedErrorDto(Map<Integer, String> codesAndMessages) {
    this.codesAndMessages = codesAndMessages;
  }

  public Map<Integer, String> getCodesAndMessages() {
    return codesAndMessages;
  }
}
