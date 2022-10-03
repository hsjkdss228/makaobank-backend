package kr.megaptera.makaobank.dtos;

import kr.megaptera.makaobank.validations.NotBlankGroup;

import javax.validation.constraints.NotBlank;

public class LoginRequestDto {
  @NotBlank(
      groups = NotBlankGroup.class,
      message = "아이디를 입력해주세요")
  private final String accountNumber;

  @NotBlank(
      groups = NotBlankGroup.class,
      message = "비밀번호를 입력해주세요")
  private final String password;

  public LoginRequestDto(String accountNumber, String password) {
    this.accountNumber = accountNumber;
    this.password = password;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public String getPassword() {
    return password;
  }
}
