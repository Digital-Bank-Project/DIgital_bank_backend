package hei.school.digital.bank.controller;

import hei.school.digital.bank.exception.InsufficientFundsException;
import hei.school.digital.bank.model.Transaction;
import hei.school.digital.bank.service.TransactionService;
import java.util.List;
import javax.security.auth.login.AccountNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/transactions")

public class TransactionController {

  private final TransactionService transactionService;

  @PostMapping("/create")
  public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
    Transaction createdTransaction = transactionService.createTransaction(transaction);
    if (createdTransaction != null) {
      return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping
  public ResponseEntity<List<Transaction>> getAllTransactions() {
    List<Transaction> transactions = transactionService.getAllTransactions();
    return new ResponseEntity<>(transactions, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
    Transaction transaction = transactionService.getTransactionById(id);
    if (transaction != null) {
      return new ResponseEntity<>(transaction, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
    transaction.setId(id);
    Transaction updatedTransaction = transactionService.updateTransaction(transaction);
    if (updatedTransaction != null) {
      return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
    transactionService.deleteTransactionById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/withdraw/{accountId}")
  public ResponseEntity<String> withdrawAmount(@PathVariable Long accountId, @RequestParam double amount, @RequestParam String motive) {
    try {
      transactionService.withdrawAmount(accountId, amount, motive);
      return ResponseEntity.ok("Withdrawal successful.");
    } catch (AccountNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (InsufficientFundsException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

}
