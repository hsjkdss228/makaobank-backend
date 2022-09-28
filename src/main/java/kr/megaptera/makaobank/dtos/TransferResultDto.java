package kr.megaptera.makaobank.dtos;

public class TransferResultDto {
  private final Long transferredAmount;

  public TransferResultDto(Long transferredAmount) {
    this.transferredAmount = transferredAmount;
  }

  public Long getTransferredAmount() {
    return transferredAmount;
  }
}
