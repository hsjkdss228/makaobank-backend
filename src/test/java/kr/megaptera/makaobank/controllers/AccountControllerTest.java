package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.exceptions.AccountNotFound;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.services.AccountService;
import kr.megaptera.makaobank.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(AccountController.class)
@ActiveProfiles("test")
class AccountControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AccountService accountService;

  @SpyBean
  private JwtUtil jwtUtil;

  @Test
  void account() throws Exception {
    AccountNumber accountNumber = new AccountNumber("352");

    given(accountService.detail(any()))
        .willReturn(Account.fake(accountNumber.value()));

    String token = jwtUtil.encode(accountNumber);

    mockMvc.perform(MockMvcRequestBuilders.get("/accounts/me")
            .header("Authorization", "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"accountNumber\":\"352\"")
        ))
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"amount\":1000000")
        ));
  }

  @Test
  void accountWithoutAccessToken() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/accounts/me"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void accountWithWrongAccessToken() throws Exception {
    String token = "wrongToken";

    mockMvc.perform(MockMvcRequestBuilders.get("/accounts/me")
            .header("Authorization", "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void accountNotFound() throws Exception {
    AccountNumber wrongAccountNumber = new AccountNumber("666666");

    given(accountService.detail(any()))
        .willThrow(new AccountNotFound(wrongAccountNumber));

    String token = jwtUtil.encode(wrongAccountNumber);

    mockMvc.perform(MockMvcRequestBuilders.get("/accounts/me")
            .header("Authorization", "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
