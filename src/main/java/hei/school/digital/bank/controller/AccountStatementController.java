package hei.school.digital.bank.controller;

import hei.school.digital.bank.model.AccountStatement;
import hei.school.digital.bank.service.AccountStatementService;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accountStatements")
@AllArgsConstructor
public class AccountStatementController {

  private final AccountStatementService accountStatementService;

  @GetMapping("/generate")
  public ResponseEntity<List<AccountStatement>> generateAccountStatements(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                          Date startDate,
                                                                          @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
    try {
      List<AccountStatement>
          accountStatements = accountStatementService.generateAccountStatements(startDate, endDate);
      return ResponseEntity.ok(accountStatements);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

}
