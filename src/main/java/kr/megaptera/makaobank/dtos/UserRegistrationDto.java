package kr.megaptera.makaobank.dtos;

import kr.megaptera.makaobank.validations.NotBlankGroup;
import kr.megaptera.makaobank.validations.PatternMatchGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserRegistrationDto {
  @NotBlank(
      groups = NotBlankGroup.class,
      message = "이름을 입력해주세요")
  @Pattern(
      groups = PatternMatchGroup.class,
      regexp = "^[가-힣]{3,7}$",
      message = "3~7자까지 한글만 사용 가능")
  private final String name;

  @NotBlank(
      groups = NotBlankGroup.class,
      message = "계좌번호로 사용될 숫자를 입력해주세요 (8글자)")
  @Pattern(
      groups = PatternMatchGroup.class,
      regexp = "^\\d{8}$",
      message = "로그인 및 거래 시 사용될 계좌번호이며 숫자만 사용 가능 (8글자)")
  private final String accountNumber;

  @NotBlank(
      groups = NotBlankGroup.class,
      message = "비밀번호를 입력해주세요")
  @Pattern(
      groups = PatternMatchGroup.class,
      regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
      message = "8글자 이상의 영문(대소문자), 숫자, 특수문자가 모두 포함되어야 함")
  private final String password;

  @NotBlank(
      groups = NotBlankGroup.class,
      message = "비밀번호 확인을 입력해주세요")
  private final String confirmPassword;

  public UserRegistrationDto(String name, String accountNumber,
                             String password, String confirmPassword) {
    this.name = name;
    this.accountNumber = accountNumber;
    this.password = password;
    this.confirmPassword = confirmPassword;
  }

  public String getName() {
    return name;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public String getPassword() {
    return password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }
}
