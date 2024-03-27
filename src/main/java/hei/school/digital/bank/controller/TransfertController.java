package hei.school.digital.bank.controller;

import hei.school.digital.bank.exception.InsufficientFundsException;
import hei.school.digital.bank.model.Transfert;
import hei.school.digital.bank.service.TransfertService;
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
@AllArgsConstructor
@RequestMapping("/transferts")

public class TransfertController {

  private TransfertService transfertService;

  @PostMapping
  public ResponseEntity<Transfert> createTransfert(@RequestBody Transfert transfert) {
    Transfert createdTransfert = transfertService.createTransfert(transfert);
    return new ResponseEntity<>(createdTransfert, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<Transfert>> getAllTransferts() {
    List<Transfert> transferts = transfertService.getAllTransferts();
    return new ResponseEntity<>(transferts, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Transfert> getTransfertById(@PathVariable Long id) {
    Transfert transfert = transfertService.getTransfertById(id);
    if (transfert != null) {
      return new ResponseEntity<>(transfert, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Transfert> updateTransfert(@PathVariable Long id, @RequestBody Transfert transfert) {
    transfert.setId(id);
    Transfert updatedTransfert = transfertService.updateTransfert(transfert);
    if (updatedTransfert != null) {
      return new ResponseEntity<>(updatedTransfert, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTransfert(@PathVariable Long id) {
    transfertService.deleteTransfertById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/transferts")
  public void transferProcess(@RequestBody Transfert transfer) {
    try {
      transfertService.manageTransfer(transfer);
    } catch (InsufficientFundsException e) {
      System.out.println("Insufficient funds to process the transfers.");
      e.printStackTrace();
    }
  }

  @PostMapping("/cancel")
  public ResponseEntity<String> cancelTransfer(@RequestBody Transfert transfer) {
    try {
      transfertService.cancelTransfer(transfer);
      return ResponseEntity.ok("Transfer canceled successfully.");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/transfers/sender/{accountId}")
  public ResponseEntity<List<Transfert>> findTransfersBySenderAccountId(@PathVariable Long accountId) {
    List<Transfert> transferts = transfertService.findTransfersBySenderAccountId(accountId);
    if (!transferts.isEmpty()) {
      return new ResponseEntity<>(transferts, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }


}
