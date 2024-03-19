package hei.school.digital.bank.service;

import hei.school.digital.bank.model.Account;
import hei.school.digital.bank.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;

  public Account createAccount(Account account) {
    return accountRepository.create(account);
  }

  public Account getAccountById(Long id) {
    return accountRepository.findById(id);
  }

  public Account updateAccount(Long id) {
    return accountRepository.update(id);
  }

  public void deleteAccountById(Long id) {
    accountRepository.deleteById(id);
  }

}
