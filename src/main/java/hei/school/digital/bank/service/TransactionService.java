package hei.school.digital.bank.service;

import hei.school.digital.bank.exception.InsufficientFundsException;
import hei.school.digital.bank.model.Account;
import hei.school.digital.bank.model.Transaction;

import hei.school.digital.bank.repository.TransactionRepository;
import java.util.List;
import javax.security.auth.login.AccountNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;

  private final AccountService accountService;


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

  public void withdrawAmount(Long accountId, double amount, String motive) throws
      AccountNotFoundException {
    Account account = accountService.getAccountById(accountId);

    // Vérifiez si le compte existe
    if (account == null) {
      throw new AccountNotFoundException("Account with ID " + accountId + " not found.");
    }

    // Calculez le solde disponible (+ le crédit autorisé)
    double availableBalance = account.getPrincipalBalance();
    if (account.isOverdraftEnabled()) {
      double authorizedCredit = accountService.calculateAuthorizedCredit(account);
      availableBalance += authorizedCredit;
    }

    // Vérifiez si le solde est suffisant pour le retrait
    if (availableBalance >= amount) {
      // Effectuez le retrait
      Transaction transaction = new Transaction();
      transaction.setAccountId(accountId);
      transaction.setAmount(amount);
      transaction.setMotive(motive);
      transaction.setType(Transaction.TransactionType.DEBIT);
      // Réalisez le retrait en mettant à jour le solde du compte
      accountService.withdrawFromAccount(account, amount);
      // Enregistrez la transaction
      createTransaction(transaction);
    } else {
      throw new InsufficientFundsException("Insufficient funds to complete the withdrawal.");
    }
  }



}
