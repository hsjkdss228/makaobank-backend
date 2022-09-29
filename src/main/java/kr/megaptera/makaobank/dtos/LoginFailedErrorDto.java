package kr.megaptera.makaobank.dtos;

public class LoginFailedErrorDto extends ErrorDto {
  public LoginFailedErrorDto() {
    super(100, "아이디 혹은 비밀번호가 맞지 않습니다");
  }
}
