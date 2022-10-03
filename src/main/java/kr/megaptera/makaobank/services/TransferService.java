package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.exceptions.AccountNotFound;
import kr.megaptera.makaobank.exceptions.TransferToMyAccount;
import kr.megaptera.makaobank.models.Account;
import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.models.Transaction;
import kr.megaptera.makaobank.repositories.AccountRepository;
import kr.megaptera.makaobank.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransferService {
  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;

  public TransferService(AccountRepository accountRepository,
                         TransactionRepository transactionRepository) {
    this.accountRepository = accountRepository;
    this.transactionRepository = transactionRepository;
  }

  public Long transfer(AccountNumber from, AccountNumber to,
                       Long amount, String name) {
    Account sender = accountRepository.findByAccountNumber(from)
        .orElseThrow(() -> new AccountNotFound(from));
    Account receiver = accountRepository.findByAccountNumber(to)
        .orElseThrow(() -> new AccountNotFound(to));

    if (sender.accountNumber().equals(receiver.accountNumber())) {
      throw new TransferToMyAccount();
    }

    sender.transferTo(receiver, amount);

    Transaction transaction = new Transaction(
      sender.accountNumber(), receiver.accountNumber(), amount, name
    );
    transactionRepository.save(transaction);

    return amount;
  }
}
