package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.dtos.AccountNotFoundErrorDto;
import kr.megaptera.makaobank.dtos.ErrorDto;
import kr.megaptera.makaobank.dtos.IncorrectAmountErrorDto;
import kr.megaptera.makaobank.dtos.InsufficientAmountErrorDto;
import kr.megaptera.makaobank.dtos.TransactionDto;
import kr.megaptera.makaobank.dtos.TransactionsDto;
import kr.megaptera.makaobank.dtos.TransferDto;
import kr.megaptera.makaobank.dtos.TransferResultDto;
import kr.megaptera.makaobank.exceptions.AccountNotFound;
import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import kr.megaptera.makaobank.exceptions.InsufficientAmount;
import kr.megaptera.makaobank.models.Transaction;
import kr.megaptera.makaobank.services.TransactionService;
import kr.megaptera.makaobank.services.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {
  private final TransferService transferService;
  private final TransactionService transactionService;

  public TransactionsController(TransferService transferService,
                                TransactionService transactionService) {
    this.transferService = transferService;
    this.transactionService = transactionService;
  }

  @GetMapping
  public TransactionsDto list() {
    List<Transaction> transactions = transactionService.list();

    // TODO: transaction >> transactionDto로 변환하는 과정을 반복해 (stream-map으로)
    //  List를 생성
    List<TransactionDto> transactionDtos = new ArrayList<>();
    return new TransactionsDto(transactionDtos);
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
