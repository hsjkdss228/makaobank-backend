package kr.megaptera.makaobank.dtos;

public class EmptyAmountErrorDto extends ErrorDto {
  public EmptyAmountErrorDto() {
    super(3001, "금액을 입력해주세요");
  }
}
