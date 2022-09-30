package kr.megaptera.makaobank.dtos;

public class EmptyNameErrorDto extends ErrorDto {
  public EmptyNameErrorDto() {
    super(3002, "입금 받는 분의 통장에 표시될 이름을 입력하세요");
  }
}
