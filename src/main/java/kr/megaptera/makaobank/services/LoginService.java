package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.exceptions.LoginFailed;
import kr.megaptera.makaobank.models.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginService {

  public Account login(String accountNumber, String password) {
    if (!accountNumber.equals("352")
        || !password.equals("password")) {
      throw new LoginFailed();
    }

    return Account.fake(accountNumber);
  }
}
