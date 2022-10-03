package kr.megaptera.makaobank.backdoor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BackdoorControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  void setupDatabase() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-database"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void changeAmount() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/change-amount?userId=1&amount=1234"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
