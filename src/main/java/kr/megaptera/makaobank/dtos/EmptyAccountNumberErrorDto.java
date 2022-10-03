package kr.megaptera.makaobank.dtos;

public class EmptyAccountNumberErrorDto extends ErrorDto {
  public EmptyAccountNumberErrorDto() {
    super(3000, "계좌번호를 입력해주세요");
  }
}
