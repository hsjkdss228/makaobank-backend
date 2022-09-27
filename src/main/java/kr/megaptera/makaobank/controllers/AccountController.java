package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.dtos.AccountDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {
  @GetMapping("/me")
  public AccountDto account() {
    return new AccountDto("황인우", "352", 1_000_000L);
  }
}
