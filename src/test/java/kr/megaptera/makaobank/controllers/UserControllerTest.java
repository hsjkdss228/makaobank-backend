package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.exceptions.RegistrationFailed;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebMvcTest(UserController.class)
class UserControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @SpyBean
  private PasswordEncoder passwordEncoder;

  @Test
  void register() throws Exception {
    AccountNumber accountNumber = new AccountNumber("35205282");
    Account account = new Account(1L, "황인우", accountNumber);
    String password = "Megaptera!1";
    account.changePassword(password, passwordEncoder);

    given(userService.create("황인우", "35205282", password, password))
        .willReturn(account);

    mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"name\":\"황인우\"," +
                "\"accountNumber\":\"35205282\"," +
                "\"password\":\"Megaptera!1\"," +
                "\"confirmPassword\":\"Megaptera!1\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isCreated());

    verify(userService).create(any(), any(), any(), any());
  }

  @Test
  void registerWithEmptyName() throws Exception {
    given(userService.create(any(), any(), any(), any()))
        .willThrow(new RegistrationFailed("이름을 입력해주세요."));

    mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"accountNumber\":\"35205282\"," +
                "\"password\":\"Megaptera!1\"," +
                "\"confirmPassword\":\"Megaptera!1\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void registerWithEmptyAccountNumber() throws Exception {
    given(userService.create(any(), any(), any(), any()))
        .willThrow(new RegistrationFailed("계좌번호로 사용될 숫자를 입력해주세요 (8글자)"));

    mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"name\":\"황인우\"," +
                "\"password\":\"Megaptera!1\"," +
                "\"confirmPassword\":\"Megaptera!1\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void registerWithEmptyPassword() throws Exception {
    given(userService.create(any(), any(), any(), any()))
        .willThrow(new RegistrationFailed("비밀번호를 입력해주세요"));

    mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"name\":\"황인우\"," +
                "\"accountNumber\":\"35205282\"," +
                "\"confirmPassword\":\"Megaptera!1\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void registerWithEmptyConfirmPassword() throws Exception {
    given(userService.create(any(), any(), any(), any()))
        .willThrow(new RegistrationFailed("비밀번호 확인을 입력해주세요"));

    mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"name\":\"황인우\"," +
                "\"accountNumber\":\"35205282\"," +
                "\"password\":\"Megaptera!1\"," +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void registerWithIncorrectName() throws Exception {
    given(userService.create(any(), any(), any(), any()))
        .willThrow(new RegistrationFailed("3~7자까지 한글만 사용 가능"));

    mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"name\":\"꽥괙괙각곽곽꽥궉꽉꿕\"," +
                "\"accountNumber\":\"35205282\"," +
                "\"password\":\"Megaptera!1\"," +
                "\"confirmPassword\":\"Megaptera!1\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void registerWithIncorrectAccountNumber() throws Exception {
    given(userService.create(any(), any(), any(), any()))
        .willThrow(new RegistrationFailed(
            "로그인 및 거래 시 사용될 계좌번호이며 숫자만 사용 가능 (8글자)"));

    mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"name\":\"황인우\"," +
                "\"accountNumber\":\"352-0528-2645-53\"," +
                "\"password\":\"Megaptera!1\"," +
                "\"confirmPassword\":\"Megaptera!1\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void registerWithIncorrectPassword() throws Exception {
    given(userService.create(any(), any(), any(), any()))
        .willThrow(new RegistrationFailed(
            "8글자 이상의 영문(대소문자), 숫자, 특수문자가 모두 포함되어야 함"));

    mockMvc.perform(MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"name\":\"황인우\"," +
                "\"accountNumber\":\"35205282\"," +
                "\"password\":\"megaptera\"," +
                "\"confirmPassword\":\"megatera\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}
