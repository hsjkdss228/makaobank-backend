package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.dtos.AccountNotFoundErrorDto;
import kr.megaptera.makaobank.dtos.EmptyAccountNumberErrorDto;
import kr.megaptera.makaobank.dtos.EmptyAmountErrorDto;
import kr.megaptera.makaobank.dtos.EmptyNameErrorDto;
import kr.megaptera.makaobank.dtos.ErrorDto;
import kr.megaptera.makaobank.dtos.IncorrectAmountErrorDto;
import kr.megaptera.makaobank.dtos.InsufficientAmountErrorDto;
import kr.megaptera.makaobank.dtos.LoginFailedErrorDto;
import kr.megaptera.makaobank.dtos.TransactionDto;
import kr.megaptera.makaobank.dtos.TransactionsDto;
import kr.megaptera.makaobank.dtos.TransferDto;
import kr.megaptera.makaobank.dtos.TransferResultDto;
import kr.megaptera.makaobank.dtos.TransferToMyAccountErrorDto;
import kr.megaptera.makaobank.exceptions.AccountNotFound;
import kr.megaptera.makaobank.exceptions.EmptyAccountNumber;
import kr.megaptera.makaobank.exceptions.EmptyAmount;
import kr.megaptera.makaobank.exceptions.EmptyName;
import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import kr.megaptera.makaobank.exceptions.InsufficientAmount;
import kr.megaptera.makaobank.exceptions.LoginFailed;
import kr.megaptera.makaobank.exceptions.RegistrationFailed;
import kr.megaptera.makaobank.exceptions.TransferToMyAccount;
import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.services.TransactionService;
import kr.megaptera.makaobank.services.TransferService;
import kr.megaptera.makaobank.validations.ValidatedSequence;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
  public TransactionsDto list(
      @RequestAttribute("accountNumber") AccountNumber accountNumber,
      @RequestParam(required = false, defaultValue = "1") Integer page
  ) {
    List<TransactionDto> transactionDtos =
        transactionService.list(accountNumber, page)
            .stream()
            .map(transaction -> transaction.toDto(accountNumber))
            .collect(Collectors.toList());

    return new TransactionsDto(transactionDtos);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TransferResultDto transfer(
      @RequestAttribute("accountNumber") AccountNumber senderAccountNumber,
      @Validated(value = {ValidatedSequence.class})
      @RequestBody TransferDto transferDto,
      BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      ObjectError error = bindingResult.getAllErrors().get(0);
      String errorMessage = error.getDefaultMessage();
      throwEmptyError(errorMessage);
    }

    AccountNumber receiverAccountNumber = new AccountNumber(transferDto.getTo());

    Long amount = transferService.transfer(
        senderAccountNumber,
        receiverAccountNumber,
        transferDto.getAmount(),
        transferDto.getName());

    return new TransferResultDto(amount);
  }

  public void throwEmptyError(String errorMessage) {
    switch (errorMessage) {
      case "계좌번호를 입력해주세요" -> throw new EmptyAccountNumber();
      case "금액을 입력해주세요" -> throw new EmptyAmount();
      case "입금 받는 분의 통장에 표시될 이름을 입력하세요" -> throw new EmptyName();
    }
  }

  @ExceptionHandler(EmptyAccountNumber.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto emptyAccountNumber() {
    return new EmptyAccountNumberErrorDto();
  }

  @ExceptionHandler(EmptyAmount.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto emptyAmount() {
    return new EmptyAmountErrorDto();
  }

  @ExceptionHandler(EmptyName.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto emptyName() {
    return new EmptyNameErrorDto();
  }

  @ExceptionHandler(AccountNotFound.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto accountNotFound() {
    return new AccountNotFoundErrorDto();
  }

  @ExceptionHandler(TransferToMyAccount.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto transferToMyAccount() {
    return new TransferToMyAccountErrorDto();
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
