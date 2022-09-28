package kr.megaptera.makaobank.repositories;

import kr.megaptera.makaobank.models.AccountNumber;
import kr.megaptera.makaobank.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  Transaction save(Transaction transaction);

  List<Transaction> findAllBySenderOrReceiverOrderByCreatedAtDesc(
      AccountNumber sender, AccountNumber receiver);
}
