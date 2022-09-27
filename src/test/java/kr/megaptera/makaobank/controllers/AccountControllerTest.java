package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.exceptions.AccountNotFound;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

  @Test
  void account() throws Exception {
    given(accountService.detail(any())).willReturn(Account.fake("352"));

    mockMvc.perform(MockMvcRequestBuilders.get("/accounts/me"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"accountNumber\":\"352\"")
        ))
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"amount\":1000000")
        ));
  }

  @Test
  void accountNotFound() throws Exception {
    given(accountService.detail(any())).willThrow(new AccountNotFound("666666"));

    mockMvc.perform(MockMvcRequestBuilders.get("/accounts/me"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
