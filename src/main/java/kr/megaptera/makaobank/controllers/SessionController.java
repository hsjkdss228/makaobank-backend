package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.dtos.ErrorDto;
import kr.megaptera.makaobank.dtos.LoginFailedErrorDto;
import kr.megaptera.makaobank.dtos.LoginRequestDto;
import kr.megaptera.makaobank.dtos.LoginResponseDto;
import kr.megaptera.makaobank.exceptions.LoginFailed;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.services.LoginService;
import kr.megaptera.makaobank.utils.JwtUtil;
import kr.megaptera.makaobank.validations.ValidatedSequence;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {
  private static final Integer EMPTY_ACCOUNT_NUMBER = 1000;
  private static final Integer EMPTY_PASSWORD = 1001;
  private static final Integer ACCOUNT_NUMBER_OR_PASSWORD_DO_NOT_MATCH = 1002;
  private static final Integer DEFAULT = 1003;

  private final LoginService loginService;
  private final JwtUtil jwtUtil;

  public SessionController(LoginService loginService, JwtUtil jwtUtil) {
    this.loginService = loginService;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public LoginResponseDto login(
      @Validated(value = {ValidatedSequence.class})
      @RequestBody LoginRequestDto loginRequestDto,
      BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      ObjectError error = bindingResult.getAllErrors().get(0);
      String errorMessage = error.getDefaultMessage();
      throw new LoginFailed(errorMessage);
    }

    AccountNumber accountNumber
        = new AccountNumber(loginRequestDto.getAccountNumber());
    String password = loginRequestDto.getPassword();

    Account account = loginService.login(
        accountNumber,
        password);

    String accessToken = jwtUtil.encode(accountNumber);

    return new LoginResponseDto(
        accessToken,
        account.name(),
        account.amount()
    );
  }

  @ExceptionHandler(LoginFailed.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto loginFailed(LoginFailed exception) {
    String errorMessage = exception.getMessage();
    Integer code = setCodeFromMessage(errorMessage);
    return new LoginFailedErrorDto(
        code,
        code.equals(DEFAULT) ? "알 수 없는 오류입니다" : errorMessage);
  }

  public Integer setCodeFromMessage(String errorMessage) {
    return switch (errorMessage) {
      case "아이디를 입력해주세요" -> EMPTY_ACCOUNT_NUMBER;
      case "비밀번호를 입력해주세요" -> EMPTY_PASSWORD;
      case "아이디 혹은 비밀번호가 맞지 않습니다" -> ACCOUNT_NUMBER_OR_PASSWORD_DO_NOT_MATCH;
      default -> DEFAULT;
    };
  }
}
