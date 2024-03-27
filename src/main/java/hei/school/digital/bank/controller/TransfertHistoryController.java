package hei.school.digital.bank.controller;

import hei.school.digital.bank.model.TransfertHistory;
import hei.school.digital.bank.service.TransfertHistoryService;
import java.util.List;
import lombok.AllArgsConstructor;
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
@RequestMapping("/transfertHistories")
@AllArgsConstructor
public class TransfertHistoryController {

  private final TransfertHistoryService transfertHistoryService;

  @GetMapping("/all")
  public List<TransfertHistory> getAllTransfertHistories() {
    return transfertHistoryService.findAllTransfertHistories();
  }

  @GetMapping("/{id}")
  public ResponseEntity<TransfertHistory> getTransfertHistoryById(@PathVariable Long id) {
    TransfertHistory transfertHistory = transfertHistoryService.findTransfertHistoryById(id);
    if (transfertHistory != null) {
      return ResponseEntity.ok(transfertHistory);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/create")
  public TransfertHistory createTransfertHistory(@RequestBody TransfertHistory transfertHistory) {
    return transfertHistoryService.createTransfertHistory(transfertHistory);
  }

  @PutMapping("/update")
  public ResponseEntity<TransfertHistory> updateTransfertHistory(@RequestBody TransfertHistory transfertHistory) {
    TransfertHistory updatedTransfertHistory = transfertHistoryService.updateTransfertHistory(transfertHistory);
    if (updatedTransfertHistory != null) {
      return ResponseEntity.ok(updatedTransfertHistory);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTransfertHistoryById(@PathVariable Long id) {
    transfertHistoryService.deleteTransfertHistoryById(id);
    return ResponseEntity.noContent().build();
  }

}
