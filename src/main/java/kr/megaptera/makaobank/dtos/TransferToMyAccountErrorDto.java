package kr.megaptera.makaobank.dtos;

public class TransferToMyAccountErrorDto extends ErrorDto {
  public TransferToMyAccountErrorDto() {
    super(3006, "본인의 계좌번호입니다. 다시 입력해주세요.");
  }
}
