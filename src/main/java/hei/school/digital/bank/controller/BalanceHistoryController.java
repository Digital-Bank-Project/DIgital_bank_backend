package hei.school.digital.bank.controller;

import hei.school.digital.bank.model.BalanceHistory;
import hei.school.digital.bank.service.BalanceHistoryService;
import java.util.List;
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
@RequestMapping("/balanceHistories")
@AllArgsConstructor
public class BalanceHistoryController {

  private final BalanceHistoryService balanceHistoryService;

  @PostMapping
  public ResponseEntity<BalanceHistory> createBalanceHistory(@RequestBody BalanceHistory balanceHistory) {
    BalanceHistory createdBalanceHistory = balanceHistoryService.createBalanceHistory(balanceHistory);
    return new ResponseEntity<>(createdBalanceHistory, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<BalanceHistory>> getAllBalanceHistory() {
    List<BalanceHistory> balanceHistories = balanceHistoryService.getAllBalanceHistory();
    return new ResponseEntity<>(balanceHistories, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BalanceHistory> getBalanceHistoryById(@PathVariable Long id) {
    BalanceHistory balanceHistory = balanceHistoryService.getBalanceHistoryById(id);
    if (balanceHistory != null) {
      return new ResponseEntity<>(balanceHistory, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<BalanceHistory> updateBalanceHistory(@PathVariable Long id, @RequestBody
  BalanceHistory balanceHistory) {
    balanceHistory.setId(id);
    BalanceHistory updatedBalanceHistory = balanceHistoryService.updateBalanceHistory(balanceHistory);
    if (updatedBalanceHistory != null) {
      return new ResponseEntity<>(updatedBalanceHistory, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBalanceHistoryById(@PathVariable Long id) {
    balanceHistoryService.deleteBalanceHistoryById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
