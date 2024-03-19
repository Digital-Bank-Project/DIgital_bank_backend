package hei.school.digital.bank.service;

import hei.school.digital.bank.model.Transaction;
import hei.school.digital.bank.repository.TransactionRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;

  public Transaction createTransaction(Transaction transaction) {
    return transactionRepository.create(transaction);
  }

  public List<Transaction> getAllTransactions() {
    return transactionRepository.findAll();
  }

  public Transaction getTransactionById(Long id) {
    return transactionRepository.findById(id);
  }

  public Transaction updateTransaction(Transaction transaction) {
    Transaction transactionToUpdate = transactionRepository.findById(transaction.getId());

    if (transactionToUpdate != null) {
      return transactionRepository.update(transaction);
    } else {
      System.out.println("Transaction with ID " + transaction.getId() + " not found.");
      return null;
    }
  }

  public void deleteTransactionById(Long id) {
    transactionRepository.deleteById(id);
  }

}
