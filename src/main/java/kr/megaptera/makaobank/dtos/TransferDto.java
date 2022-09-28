package kr.megaptera.makaobank.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TransferDto {
  @NotBlank
  private String to;

  @NotNull
  private Long amount;

  @NotBlank
  private String name;

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
