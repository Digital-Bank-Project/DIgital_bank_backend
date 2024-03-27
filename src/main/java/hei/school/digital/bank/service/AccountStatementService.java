package hei.school.digital.bank.service;

import hei.school.digital.bank.model.AccountStatement;
import hei.school.digital.bank.model.Transaction;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountStatementService {

  private final TransactionService transactionService;

  public List<AccountStatement> generateAccountStatements(Date startDate, Date endDate) {
    List<Transaction> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);

    // Regroupement des transactions par date
    Map<LocalDate, List<Transaction>> groupedTransactions = transactions.stream()
        .collect(Collectors.groupingBy(transaction -> transaction.getDateTime().toLocalDate()));

    List<AccountStatement> accountStatements = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");


    // Map des transactions vers les relevés de compte
    for (List<Transaction> transactionGroup : groupedTransactions.values()) {
      for (Transaction transaction : transactionGroup) {
        AccountStatement accountStatement = new AccountStatement();
        accountStatement.setAccountId(transaction.getAccountId());
        accountStatement.setEffectiveDate(Date.from(transaction.getDateTime().atZone(ZoneId.systemDefault()).toInstant()));
        accountStatement.setMotive(transaction.getMotive());
        accountStatement.setCreditMga(transaction.getType() == Transaction.TransactionType.CREDIT ? transaction.getAmount() : 0);
        accountStatement.setDebitMga(transaction.getType() == Transaction.TransactionType.DEBIT ? transaction.getAmount() : 0);

        // Génération de la référence
        String referenceDate = dateFormat.format(transaction.getDateTime());
        int occurrence = transactionGroup.size() > 1 ? getTransactionOccurrence(transactionGroup, transaction) : 1;
        String reference = "VIR_" + referenceDate + "_" + String.format("%02d", occurrence);
        accountStatement.setRef(reference);

        accountStatements.add(accountStatement);
      }
    }

    // Trier les relevés de compte par ordre décroissant de la date de prise d'effet
    accountStatements.sort(Comparator.comparing(AccountStatement::getEffectiveDate).reversed());

    // Calculer le solde pour chaque transaction
    double balance = 0;
    for (AccountStatement accountStatement : accountStatements) {
      balance += accountStatement.getCreditMga() - accountStatement.getDebitMga();
      accountStatement.setPrincipalBalance(balance);
    }

    return accountStatements;
  }

  private int getTransactionOccurrence(List<Transaction> transactions, Transaction transaction) {
    int occurrence = 0;
    for (Transaction trans : transactions) {
      if (trans.getId().equals(transaction.getId())) {
        occurrence++;
        if (trans == transaction) {
          break;
        }
      }
    }
    return occurrence;
  }


}
