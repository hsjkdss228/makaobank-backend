package kr.megaptera.makaobank.dtos;

public class InsufficientAmountErrorDto extends ErrorDto {
  public InsufficientAmountErrorDto() {
    super(1003, "계좌 잔액이 부족합니다.");
  }
}
