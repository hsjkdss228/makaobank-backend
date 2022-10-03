package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.dtos.AccountDto;
import kr.megaptera.makaobank.dtos.RegistrationFailedErrorDto;
import kr.megaptera.makaobank.dtos.UserRegistrationDto;
import kr.megaptera.makaobank.exceptions.RegistrationFailed;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.services.UserService;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
  private static final Integer EMPTY_NAME = 2000;
  private static final Integer EMPTY_ACCOUNT_NUMBER = 2001;
  private static final Integer EMPTY_PASSWORD = 2002;
  private static final Integer EMPTY_CONFIRM_PASSWORD = 2003;
  private static final Integer INCORRECT_NAME = 2004;
  private static final Integer INCORRECT_ACCOUNT_NUMBER = 2005;
  private static final Integer INCORRECT_PASSWORD = 2006;
  private static final Integer ALREADY_EXISTING_ACCOUNT_NUMBER = 2007;
  private static final Integer PASSWORD_DO_NOT_MATCH = 2008;
  private static final Integer DEFAULT = 2009;

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AccountDto register(
      @Validated(value = {ValidatedSequence.class})
      @RequestBody UserRegistrationDto userRegistrationDto,
      BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      List<String> errors = new ArrayList<>();
      for (ObjectError error : bindingResult.getAllErrors()) {
        String errorMessage = error.getDefaultMessage();
        errors.add(errorMessage);
      }
      throw new RegistrationFailed(errors);
    }

    Account account = userService.create(
        userRegistrationDto.getName(),
        userRegistrationDto.getAccountNumber(),
        userRegistrationDto.getPassword(),
        userRegistrationDto.getConfirmPassword()
    );

    return account.toDto();
  }

  @ExceptionHandler(RegistrationFailed.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public RegistrationFailedErrorDto registrationFailed(RegistrationFailed exception) {
    Map<Integer, String> codesAndMessages = new LinkedHashMap<>();
    for (String errorMessage : exception.errorMessages()) {
      Integer code = setCodeFromMessage(errorMessage);
      codesAndMessages.put(
          code,
          code.equals(DEFAULT) ? "알 수 없는 오류입니다" : errorMessage);
    }

    return new RegistrationFailedErrorDto(codesAndMessages);
  }

  public Integer setCodeFromMessage(String errorMessage) {
    return switch (errorMessage) {
      case "이름을 입력해주세요" -> EMPTY_NAME;
      case "계좌번호로 사용될 숫자를 입력해주세요 (8글자)" -> EMPTY_ACCOUNT_NUMBER;
      case "비밀번호를 입력해주세요" -> EMPTY_PASSWORD;
      case "비밀번호 확인을 입력해주세요" -> EMPTY_CONFIRM_PASSWORD;
      case "3~7자까지 한글만 사용 가능" -> INCORRECT_NAME;
      case "로그인 및 거래 시 사용될 계좌번호이며 숫자만 사용 가능 (8글자)" -> INCORRECT_ACCOUNT_NUMBER;
      case "8글자 이상의 영문(대소문자), 숫자, 특수문자가 모두 포함되어야 함" -> INCORRECT_PASSWORD;
      case "이미 존재하는 계좌번호입니다" -> ALREADY_EXISTING_ACCOUNT_NUMBER;
      case "비밀번호가 일치하지 않습니다" -> PASSWORD_DO_NOT_MATCH;
      default -> DEFAULT;
    };
  }
}
