package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.dtos.AccountNotFoundErrorDto;
import kr.megaptera.makaobank.dtos.ErrorDto;
import kr.megaptera.makaobank.dtos.IncorrectAmountErrorDto;
import kr.megaptera.makaobank.dtos.InsufficientAmountErrorDto;
import kr.megaptera.makaobank.dtos.TransferDto;
import kr.megaptera.makaobank.dtos.TransferResultDto;
import kr.megaptera.makaobank.exceptions.AccountNotFound;
import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import kr.megaptera.makaobank.exceptions.InsufficientAmount;
import kr.megaptera.makaobank.services.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {
  private final TransferService transferService;

  public TransactionsController(TransferService transferService) {
    this.transferService = transferService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TransferResultDto transfer(
      @Validated @RequestBody TransferDto transferDto
  ) {
    // TODO: 보내는 사람을 인증 후 제대로 가져오도록 해야 함
    String accountNumber = "352";

    Long amount = transferService.transfer(
        accountNumber,
        transferDto.getTo(),
        transferDto.getAmount());

    return new TransferResultDto(amount);
  }

  @ExceptionHandler(AccountNotFound.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto accountNotFound() {
    return new AccountNotFoundErrorDto();
  }

  @ExceptionHandler(IncorrectAmount.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto incorrectAmount() {
    return new IncorrectAmountErrorDto();
  }

  @ExceptionHandler(InsufficientAmount.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto insufficientAmount() {
    return new InsufficientAmountErrorDto();
  }
}
