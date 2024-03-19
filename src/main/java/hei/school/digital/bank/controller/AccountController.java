package hei.school.digital.bank.controller;

import hei.school.digital.bank.model.Account;
import hei.school.digital.bank.service.AccountService;
import java.util.List;
import java.util.Objects;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor

public class AccountController {

  private final AccountService accountService;

  @PostMapping
  public ResponseEntity<Account> createAccount(@RequestBody Account account) {
    Account createdAccount = accountService.createAccount(account);
    return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<Account>> getAllAccounts() {
    List<Account> accounts = accountService.getAllAccounts();
    if (!accounts.isEmpty()) {
      return new ResponseEntity<>(accounts, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
    Account account = accountService.getAccountById(id);
    if (account != null) {
      return new ResponseEntity<>(account, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account account) {
    if (!Objects.equals(id, account.getId())) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    Account updatedAccount = accountService.updateAccount(account);

    if (updatedAccount != null) {
      return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAccountById(@PathVariable Long id) {
    accountService.deleteAccountById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }


}
