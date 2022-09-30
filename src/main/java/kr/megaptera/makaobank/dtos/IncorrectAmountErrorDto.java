package kr.megaptera.makaobank.dtos;

public class IncorrectAmountErrorDto extends ErrorDto {
  public IncorrectAmountErrorDto() {
    super(3004, "금액이 잘못됐습니다");
  }
}
