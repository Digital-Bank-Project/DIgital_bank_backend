package hei.school.digital.bank.controller;

import hei.school.digital.bank.model.OverdraftInterest;
import hei.school.digital.bank.service.OverdraftInterestService;
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
@RequestMapping("/overdraftInterests")
@AllArgsConstructor
public class OverdraftInterestController {

  private final OverdraftInterestService overdraftInterestService;

  @PostMapping
  public ResponseEntity<OverdraftInterest> createOverdraftInterest(@RequestBody OverdraftInterest overdraftInterest) {
    OverdraftInterest createdOverdraftInterest = overdraftInterestService.createOverdraftInterest(overdraftInterest);
    return new ResponseEntity<>(createdOverdraftInterest, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<OverdraftInterest>> getAllOverdraftInterests() {
    List<OverdraftInterest> overdraftInterests = overdraftInterestService.getAllOverdraftInterests();
    return new ResponseEntity<>(overdraftInterests, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OverdraftInterest> getOverdraftInterestById(@PathVariable Long id) {
    OverdraftInterest overdraftInterest = overdraftInterestService.getOverdraftInterestById(id);
    if (overdraftInterest != null) {
      return new ResponseEntity<>(overdraftInterest, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<OverdraftInterest> updateOverdraftInterest(@PathVariable Long id, @RequestBody
  OverdraftInterest overdraftInterest) {
    overdraftInterest.setId(id);
    OverdraftInterest updatedOverdraftInterest = overdraftInterestService.updateOverdraftInterest(overdraftInterest);
    if (updatedOverdraftInterest != null) {
      return new ResponseEntity<>(updatedOverdraftInterest, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOverdraftInterestById(@PathVariable Long id) {
    overdraftInterestService.deleteOverdraftInterestById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }



}
