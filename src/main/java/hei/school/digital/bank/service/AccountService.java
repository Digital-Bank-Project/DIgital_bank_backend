package hei.school.digital.bank.service;

import hei.school.digital.bank.exception.InsufficientFundsException;
import hei.school.digital.bank.model.Account;
import hei.school.digital.bank.model.BalanceHistory;
import hei.school.digital.bank.repository.AccountRepository;
import hei.school.digital.bank.utils.AccountNumberGenerator;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final BalanceHistoryService balanceHistoryService;

  public Account createAccount(Account account) {
    String accountNumber = AccountNumberGenerator.generateAccountNumber();
    account.setAccountNumber(accountNumber);
    Account createdAccount = accountRepository.create(account);


    if (createdAccount != null) {

      BalanceHistory initialBalanceHistory = new BalanceHistory();
      initialBalanceHistory.setAccountId(createdAccount.getId());
      initialBalanceHistory.setPrincipalBalance(createdAccount.getPrincipalBalance());
      initialBalanceHistory.setLoanAmount(0);
      initialBalanceHistory.setInterestAmount(0);
      initialBalanceHistory.setTimestamp( LocalDateTime.now());
      balanceHistoryService.createBalanceHistory(initialBalanceHistory);
    }
    return createdAccount;

  }

  public List<Account> getAllAccounts(){
    return accountRepository.findAll();
  }

  public Account getAccountById(Long id) {
    return accountRepository.findById(id);
  }

  public Account updateAccount(Account account) {
    Account updatedAccount = accountRepository.findById(account.getId());

    if (updatedAccount != null) {
       accountRepository.update(account);
      BalanceHistory updatedBalanceHistory = new BalanceHistory();
      updatedBalanceHistory.setAccountId(updatedAccount.getId());
      updatedBalanceHistory.setPrincipalBalance(updatedAccount.getPrincipalBalance());
      updatedBalanceHistory.setTimestamp(LocalDateTime.now());
      balanceHistoryService.createBalanceHistory(updatedBalanceHistory);

      return updatedAccount;
    } else {
      System.out.println("Account with ID " + account.getId() + " not found.");
      return null;
    }
  }

  public void deleteAccountById(Long id) {
    accountRepository.deleteById(id);
  }

}
