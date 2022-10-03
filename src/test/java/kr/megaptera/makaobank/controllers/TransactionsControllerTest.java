package kr.megaptera.makaobank.controllers;

import kr.megaptera.makaobank.exceptions.AccountNotFound;
import kr.megaptera.makaobank.exceptions.IncorrectAmount;
import kr.megaptera.makaobank.exceptions.InsufficientAmount;
import kr.megaptera.makaobank.exceptions.TransferToMyAccount;
import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.models.Transaction;
import kr.megaptera.makaobank.services.TransactionService;
import kr.megaptera.makaobank.services.TransferService;
import kr.megaptera.makaobank.utils.JwtUtil;
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

  @SpyBean
  private JwtUtil jwtUtil;

  // Tests of Transactions
  @Test
  void list() throws Exception {
    Transaction transaction = mock(Transaction.class);
    int page = 1;

    AccountNumber accountNumber = new AccountNumber("352");
    given(transactionService.list(accountNumber, page))
        .willReturn(List.of(
            transaction
        ));

    String token = jwtUtil.encode(accountNumber);

    mockMvc.perform(MockMvcRequestBuilders.get("/transactions")
            .header("Authorization", "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"transactions\":[")
        ));

    verify(transactionService).list(accountNumber, page);
  }

  // Tests of Transfer
  @Test
  void transfer() throws Exception {
    AccountNumber senderAccountNumber = new AccountNumber("352");
    AccountNumber receiverAccountNumber = new AccountNumber("179");
    String name = "김인우";

    String token = jwtUtil.encode(senderAccountNumber);

    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"" + receiverAccountNumber.value() + "\"," +
                "\"amount\":\"3000\"," +
                "\"name\":\"" + name + "\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isCreated());

    verify(transferService).transfer(
        senderAccountNumber, receiverAccountNumber, 3_000L, name);
  }

  @Test
  void transferWithEmptyAccountNumber() throws Exception {
    AccountNumber senderAccountNumber = new AccountNumber("352");
    String name = "김인우";

    String token = jwtUtil.encode(senderAccountNumber);

    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"amount\":\"3000\"," +
                "\"name\":\"" + name + "\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"code\":3000")
        ));
  }

  @Test
  void transferWithEmptyAmount() throws Exception {
    AccountNumber senderAccountNumber = new AccountNumber("352");
    AccountNumber receiverAccountNumber = new AccountNumber("179");
    String name = "김인우";

    String token = jwtUtil.encode(senderAccountNumber);

    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"" + receiverAccountNumber.value() + "\"," +
                "\"name\":\"" + name + "\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"code\":3001")
        ));
  }

  @Test
  void transferWithEmptyName() throws Exception {
    AccountNumber senderAccountNumber = new AccountNumber("352");
    AccountNumber receiverAccountNumber = new AccountNumber("179");

    String token = jwtUtil.encode(senderAccountNumber);

    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"" + receiverAccountNumber.value() + "\"," +
                "\"amount\":\"3000\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"code\":3002")
        ));
  }

  @Test
  void transferWithIncorrectAccountNumber() throws Exception {
    AccountNumber senderAccountNumber = new AccountNumber("352");
    AccountNumber incorrectAccountNumber = new AccountNumber("666666");
    String name = "김인우";

    given(transferService.transfer(any(), any(), any(), any()))
        .willThrow(new AccountNotFound(incorrectAccountNumber));

    String token = jwtUtil.encode(senderAccountNumber);

    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"" + incorrectAccountNumber.value() + "\"," +
                "\"amount\":\"3000\"," +
                "\"name\":\"" + name + "\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"code\":3003")
        ));
  }

  @Test
  void transferWithNegativeAmount() throws Exception {
    AccountNumber senderAccountNumber = new AccountNumber("352");
    Long negativeAmount = -10L;
    String name = "김인우";

    given(transferService.transfer(any(), any(), any(), any()))
        .willThrow(new IncorrectAmount(negativeAmount));

    String token = jwtUtil.encode(senderAccountNumber);

    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"179\"," +
                "\"amount\":\"" + negativeAmount + "\"," +
                "\"name\":\"" + name + "\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"code\":3004")
        ));
  }

  @Test
  void transferWithTooLargeAmount() throws Exception {
    AccountNumber senderAccountNumber = new AccountNumber("352");
    Long tooLargeAmount = 45_600_000_000L;
    String name = "김인우";

    given(transferService.transfer(any(), any(), any(), any()))
        .willThrow(new InsufficientAmount(tooLargeAmount));

    String token = jwtUtil.encode(senderAccountNumber);

    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"179\"," +
                "\"amount\":\"" + tooLargeAmount + "\"," +
                "\"name\":\"" + name + "\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"code\":3005")
        ));
  }

  @Test
  void transferToMyAccount() throws Exception {
    AccountNumber senderAccountNumber = new AccountNumber("352");
    String name = "김인우";

    given(transferService.transfer(any(), any(), any(), any()))
        .willThrow(new TransferToMyAccount());

    String token = jwtUtil.encode(senderAccountNumber);

    mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{" +
                "\"to\":\"" + senderAccountNumber.value() + "\"," +
                "\"amount\":\"3000\"," +
                "\"name\":\"" + name + "\"" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("\"code\":3006")
        ));
  }
}
