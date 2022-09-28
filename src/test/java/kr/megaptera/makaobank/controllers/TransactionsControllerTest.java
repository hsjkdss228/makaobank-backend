package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.exceptions.AccountNotFound;
import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import kr.megaptera.makaobank.exceptions.InsufficientAmount;
import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.models.Transaction;
import kr.megaptera.makaobank.services.TransactionService;
import kr.megaptera.makaobank.services.TransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@WebMvcTest(TransactionsController.class)
@ActiveProfiles("test")
class TransactionsControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TransferService transferService;

  @MockBean
  private TransactionService transactionService;

  // Tests of Transactions
  @Test
  void list() throws Exception {
    Transaction transaction = mock(Transaction.class);

    given(transactionService.list())
        .willReturn(List.of(
            transaction
        ));

    mockMvc.perform(MockMvcRequestBuilders.get("/transactions"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"transactions\":[")
        ));

    verify(transactionService).list();
  }

  // Tests of Transfer
  @Test
  void transfer() throws Exception {
    AccountNumber receiverAccountNumber = new AccountNumber("179");
    String name = "김인우";

    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"" + receiverAccountNumber.value() + "\"," +
                "\"amount\":\"3000\"," +
                "\"name\":\"" + name + "\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isCreated());

    verify(transferService).transfer(
        new AccountNumber("352"), receiverAccountNumber, 3_000L, name);
  }

  @Test
  void transferWithIncorrectAccountNumber() throws Exception {
    AccountNumber incorrectAccountNumber = new AccountNumber("666666");
    String name = "김인우";

    given(transferService.transfer(any(), any(), any(), any()))
        .willThrow(new AccountNotFound(incorrectAccountNumber));

    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"" + incorrectAccountNumber.value() + "\"," +
                "\"amount\":\"3000\"," +
                "\"name\":\"" + name + "\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"code\":1001")
        ));
  }

  @Test
  void transferWithNegativeAmount() throws Exception {
    Long negativeAmount = -10L;
    String name = "김인우";

    given(transferService.transfer(any(), any(), any(), any()))
        .willThrow(new IncorrectAmount(negativeAmount));

    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"179\"," +
                "\"amount\":\"" + negativeAmount + "\"," +
                "\"name\":\"" + name + "\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"code\":1002")
        ));
  }

  @Test
  void transferWithTooLargeAmount() throws Exception {
    Long tooLargeAmount = 45_600_000_000L;
    String name = "김인우";

    given(transferService.transfer(any(), any(), any(), any()))
        .willThrow(new InsufficientAmount(tooLargeAmount));

    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"179\"," +
                "\"amount\":\"" + tooLargeAmount + "\"," +
                "\"name\":\"" + name + "\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"code\":1003")
        ));
  }
}
