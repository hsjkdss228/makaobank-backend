package kr.megaptera.makaobank.dtos;

public class TransferResultDto {
  private Long transferredAmount;

  public TransferResultDto() {

  }

  public TransferResultDto(Long transferredAmount) {
    this.transferredAmount = transferredAmount;
  }

  public Long getTransferredAmount() {
    return transferredAmount;
  }
}
