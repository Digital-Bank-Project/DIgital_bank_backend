package hei.school.digital.bank.service;

import hei.school.digital.bank.exception.InsufficientFundsException;
import hei.school.digital.bank.model.Account;
import hei.school.digital.bank.model.BalanceHistory;
import hei.school.digital.bank.model.Transaction;

import hei.school.digital.bank.model.Transfert;
import hei.school.digital.bank.repository.TransactionRepository;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.security.auth.login.AccountNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final AccountService accountService;
  private final BalanceHistoryService balanceHistoryService;

  private final TransfertService transfertService;
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

  public void depositAmount(Long accountId, double amount, String reason) throws AccountNotFoundException {
    Account account = accountService.getAccountById(accountId);

    // Vérifier si le compte existe
    if (account == null) {
      throw new AccountNotFoundException("Account with ID " + accountId + " not found.");
    }

    // Vérifier si le compte a un prêt dans l'historique des soldes
    BalanceHistory lastBalanceHistory = balanceHistoryService.findLastBalanceHistoryByAccountId(accountId);
    if (lastBalanceHistory != null && lastBalanceHistory.getLoanAmount() > 0) {
      // Le compte a un prêt positif dans l'historique des soldes
      // Soustraire le dépôt du montant du prêt
      double remainingAmount = amount - lastBalanceHistory.getLoanAmount();
      if (remainingAmount >= 0) {
        // Le dépôt couvre le prêt, ajuster le solde principal
        account.setPrincipalBalance(account.getPrincipalBalance() + remainingAmount);
      } else {
        // Le dépôt ne couvre pas le prêt, le solde principal reste à zéro
        // Le montant du prêt reste positif dans l'historique des soldes
        lastBalanceHistory.setLoanAmount(-remainingAmount);
        balanceHistoryService.updateBalanceHistory(lastBalanceHistory);
      }
    } else {
      // Le compte n'a pas de prêt dans l'historique des soldes
      // Ajouter le montant du dépôt au solde principal
      account.setPrincipalBalance(account.getPrincipalBalance() + amount);
    }

    // Mettre à jour le compte
    accountService.updateAccount(account);

    // Enregistrer les détails du transfert
    Transfert transfert = new Transfert();
    transfert.setSenderAccountId(accountId);
    transfert.setRecipientAccountId(accountId);
    transfert.setAmount(amount);
    transfert.setReason(reason);
    transfert.setEffectiveDate(LocalDateTime.now());
    transfert.setRegistrationDate(LocalDateTime.now());
    transfert.setStatus(Transfert.TransfertStatus.COMPLETED);

    transfertService.createTransfert(transfert);

    // Enregistrer une nouvelle transaction de type crédit
    Transaction transaction = new Transaction();
    transaction.setAccountId(accountId);
    transaction.setAmount(amount);
    transaction.setMotive(reason);
    transaction.setType(Transaction.TransactionType.CREDIT);
    transaction.setDateTime(LocalDateTime.now());
    createTransaction(transaction);
  }

  public List<Transaction> getTransactionsByDateRange(Date startDate, Date endDate){
    return transactionRepository.getTransactionsByDateRange(startDate,endDate);
  }

}
