package kr.megaptera.makaobank.repositories;

import kr.megaptera.makaobank.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  Transaction save(Transaction transaction);
}
