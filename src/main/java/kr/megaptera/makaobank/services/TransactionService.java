package kr.megaptera.makaobank.services;

import kr.megaptera.makaobank.models.Transaction;
import kr.megaptera.makaobank.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransactionService {
  private final TransactionRepository transactionRepository;

  public TransactionService(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  public List<Transaction> list() {
    List<Transaction> transactions = transactionRepository.findAll();

    return transactions;
  }
}
