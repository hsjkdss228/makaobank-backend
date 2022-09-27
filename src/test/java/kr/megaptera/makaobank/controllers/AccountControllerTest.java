package kr.megaptera.makaobank.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  void account() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/accounts/me"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("name")
        ))
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("accountNumber")
        ))
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("amount")
        ));
  }
}