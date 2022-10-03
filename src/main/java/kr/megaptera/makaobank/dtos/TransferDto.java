package kr.megaptera.makaobank.dtos;

import kr.megaptera.makaobank.validations.NotBlankGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TransferDto {
  @NotBlank(
      groups = NotBlankGroup.class,
      message = "계좌번호를 입력해주세요")
  private final String to;

  @NotNull(
      groups = NotBlankGroup.class,
      message = "금액을 입력해주세요")
  private final Long amount;

  @NotBlank(
      groups = NotBlankGroup.class,
      message = "입금 받는 분의 통장에 표시될 이름을 입력하세요")
  private final String name;

  public TransferDto(String to, Long amount, String name) {
    this.to = to;
    this.amount = amount;
    this.name = name;
  }

  public String getTo() {
    return to;
  }

  public Long getAmount() {
    return amount;
  }

  public String getName() {
    return name;
  }
}
