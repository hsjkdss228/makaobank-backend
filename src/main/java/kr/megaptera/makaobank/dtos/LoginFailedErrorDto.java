package kr.megaptera.makaobank.dtos;

public class LoginFailedErrorDto extends ErrorDto {
  public LoginFailedErrorDto(Integer code, String message) {
    super(code, message);
  }
}
