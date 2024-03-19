package hei.school.digital.bank.service;

import hei.school.digital.bank.model.Account;
import hei.school.digital.bank.repository.AccountRepository;
import hei.school.digital.bank.utils.AccountNumberGenerator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;

  public Account createAccount(Account account) {
    String accountNumber = AccountNumberGenerator.generateAccountNumber();
    account.setAccountNumber(accountNumber);

    return accountRepository.create(account);
  }

  public List<Account> getAllAccounts(){
    return accountRepository.findAll();
  }

  public Account getAccountById(Long id) {
    return accountRepository.findById(id);
  }

  public Account updateAccount(Account account) {
    Account accountToUpdate = accountRepository.findById(account.getId());

    if (accountToUpdate != null) {
      return accountRepository.update(account);
    } else {
      System.out.println("Account with ID " + account.getId() + " not found.");
      return null;
    }
  }


  public void deleteAccountById(Long id) {
    accountRepository.deleteById(id);
  }

}
