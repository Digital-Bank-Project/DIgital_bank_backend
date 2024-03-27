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

  public void withdrawFromAccount(Account account, double amount) {
    double balance = account.getPrincipalBalance();

    // Perform withdrawal normally
    if (balance >= amount) {
      account.setPrincipalBalance(balance - amount);
      updateAccount(account);
    } else if (account.isOverdraftEnabled()) {
      double authorizedCredit = calculateAuthorizedCredit(account);
      double availableBalance = balance + authorizedCredit;

      // Check if withdrawal exceeds authorized balance
      if (availableBalance >= amount) {
        // Withdraw with overdraft
        account.setPrincipalBalance(0);
        updateAccount(account);
        double remainingAmount = amount - balance;

        // Add loan amount to balance history
        addToBalanceHistory(account, remainingAmount);

        // Record overdraft start date if balance becomes negative
        if (balance >= 0 && hasNegativeBalance(account)) {
          Timestamp overdraftStartDate = new Timestamp(System.currentTimeMillis());
          account.setOverdraftStartDate(overdraftStartDate);
          updateAccount(account);
        }
      } else {
        throw new InsufficientFundsException("Insufficient funds to complete the withdrawal.");
      }
    } else {
      throw new InsufficientFundsException("Insufficient funds to complete the withdrawal.");
    }

    // Apply overdraft interest if balance is negative and overdraft start date is recorded
    BalanceHistory lastBalanceHistoryOptional = balanceHistoryService.findLastBalanceHistoryByAccountId(account.getId());

    if (lastBalanceHistoryOptional.getLoanAmount() > 0 && account.getOverdraftStartDate() != null) {
      LocalDate currentDate = LocalDate.now();
      Timestamp overdraftStartDate = account.getOverdraftStartDate();
      long daysOverdrawn = ChronoUnit.DAYS.between(overdraftStartDate.toLocalDateTime().toLocalDate(), currentDate);

      // Calculate overdraft interest based on number of days
      double interestRate;
      if (daysOverdrawn <= 7) {
        interestRate = account.getInterestDetails().getInterestRateForFirst7Days();
      } else {
        interestRate = account.getInterestDetails().getInterestRateAfter7Days();
      }
      double overdraftInterest = account.getPrincipalBalance() * (interestRate / 100);

      // Add overdraft interest to account balance
      account.setPrincipalBalance(account.getPrincipalBalance() + overdraftInterest);
      updateAccount(account);
    }
  }

  public double calculateAuthorizedCredit(Account account) {
    double monthlySalary = account.getMonthlySalary();

    double authorizedCredit = monthlySalary / 3.0;

    return authorizedCredit;
  }

  // Add transaction to balance history
  private void addToBalanceHistory(Account account, double loanAmount) {
    BalanceHistory balanceHistory = new BalanceHistory();
    balanceHistory.setAccountId(account.getId());
    balanceHistory.setPrincipalBalance(account.getPrincipalBalance());
    balanceHistory.setLoanAmount(loanAmount);
    balanceHistory.setInterestAmount(account.getOverdraftInterest());
    balanceHistory.setTimestamp(LocalDateTime.now());
    balanceHistoryService.updateBalanceHistory(balanceHistory);
  }

  // Check if balance becomes negative after transaction
  private boolean hasNegativeBalance(Account account) {
    // Retrieve account balance history
    List<BalanceHistory> balanceHistory = balanceHistoryService.findByAccountId(account.getId());

    // Check if balance becomes negative in history
    for (BalanceHistory entry : balanceHistory) {
      if (entry.getLoanAmount() > 0) {
        return true;
      }
    }
    return false;
  }


}
