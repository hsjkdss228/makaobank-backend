package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.dtos.AccountDto;
import kr.megaptera.makaobank.exceptions.AccountNotFound;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {
  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping("/me")
  public AccountDto account() {
    // TODO: 사용자 계좌번호는 인증 정보를 활용해 가져와야 함
    AccountNumber accountNumber = new AccountNumber("352");
    Account account = accountService.detail(accountNumber);
    return account.toDto();
  }

  @ExceptionHandler(AccountNotFound.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String accountNotFound() {
    return "Account not found";
  }
}
