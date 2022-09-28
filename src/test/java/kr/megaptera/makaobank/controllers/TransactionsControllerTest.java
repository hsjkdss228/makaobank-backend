package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.exceptions.AccountNotFound;
import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import kr.megaptera.makaobank.exceptions.InsufficientAmount;
import kr.megaptera.makaobank.services.TransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebMvcTest(TransactionsController.class)
@ActiveProfiles("test")
class TransactionsControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TransferService transferService;

  @Test
  void transfer() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"179\"," +
                "\"amount\":\"3000\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isCreated());

    verify(transferService).transfer("352", "179", 3_000L);
  }

  @Test
  void transferWithIncorrectAccountNumber() throws Exception {
    String incorrectAccountNumber = "666666";

    given(transferService.transfer(any(), any(), any()))
        .willThrow(new AccountNotFound(incorrectAccountNumber));

    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"" + incorrectAccountNumber + "\"," +
                "\"amount\":\"3000\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"code\":1001")
        ));
  }

  @Test
  void transferWithNegativeAmount() throws Exception {
    Long negativeAmount = -10L;

    given(transferService.transfer(any(), any(), any()))
        .willThrow(new IncorrectAmount(negativeAmount));

    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"179\"," +
                "\"amount\":\"" + negativeAmount + "\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"code\":1002")
        ));
  }

  @Test
  void transferWithTooLargeAmount() throws Exception {
    Long tooLargeAmount = 45_600_000_000L;

    given(transferService.transfer(any(), any(), any()))
        .willThrow(new InsufficientAmount(tooLargeAmount));

    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"179\"," +
                "\"amount\":\"" + tooLargeAmount + "\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"code\":1003")
        ));
  }
}
