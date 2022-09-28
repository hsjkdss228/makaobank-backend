package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.exceptions.LoginFailed;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.services.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;

@WebMvcTest(SessionController.class)
class SessionControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LoginService loginService;

  @BeforeEach
  void setUp() {
    AccountNumber accountNumber = new AccountNumber("352");
    AccountNumber wrongAccountNumber = new AccountNumber("wrongAccountNumber");

    given(loginService.login(accountNumber, "password"))
        .willReturn(Account.fake(accountNumber.value()));
    given(loginService.login(wrongAccountNumber, "password"))
        .willThrow(LoginFailed.class);
    given(loginService.login(accountNumber, "wrongPassword"))
        .willThrow(LoginFailed.class);
  }

  @Test
  void loginSuccess() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/session")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"accountNumber\":\"352\"," +
                "\"password\":\"password\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"amount\":")
        ));
  }

  @Test
  void loginFailWithWrongAccountNumber() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/session")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"accountNumber\":\"wrongAccountNumber\"," +
                "\"password\":\"password\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void loginFailWithWrongPassword() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/session")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"accountNumber\":\"352\"," +
                "\"password\":\"wrongPassword\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}
