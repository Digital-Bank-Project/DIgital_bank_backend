package hei.school.digital.bank.service;

import hei.school.digital.bank.exception.InsufficientFundsException;
import hei.school.digital.bank.model.Account;
import hei.school.digital.bank.model.Transaction;
import hei.school.digital.bank.model.Transfert;
import hei.school.digital.bank.model.TransfertHistory;
import hei.school.digital.bank.repository.TransfertRepository;
import hei.school.digital.bank.transferSchedule.TransfertScheduleTaskBank;
import hei.school.digital.bank.transferSchedule.TransfertScheduler;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class TransfertService {

  private final TransfertRepository transfertRepository;
  private final AccountService accountService;
  private final TransactionService transactionService;

  private final TransfertHistoryService transfertHistoryService;

  private final TransfertScheduleTaskBank transfertScheduleTask;

  private final TransfertScheduler transfertScheduler;

  public Transfert createTransfert(Transfert transfert) {

    Transfert createdTransfert = transfertRepository.create(transfert);

    // Si le statut du transfert est "completed", enregistrer un nouveau transfertHistory
    if (createdTransfert.getStatus() == Transfert.TransfertStatus.COMPLETED) {
      TransfertHistory transfertHistory = new TransfertHistory();
      transfertHistory.setTransferId(createdTransfert.getId());
      transfertHistory.setTransfertStatus(TransfertHistory.TransfertHistoryStatus.COMPLETED);
      transfertHistoryService.createTransfertHistory(transfertHistory);
    }

    return createdTransfert;  }

  public List<Transfert> getAllTransferts() {
    return transfertRepository.findAll();
  }

  public Transfert getTransfertById(Long id) {
    return transfertRepository.findById(id);
  }

  public Transfert updateTransfert(Transfert transfert) {
    Transfert updatedTransfert = transfertRepository.update(transfert);

    // Si le statut du transfert est "completed", enregistrer un nouveau transfertHistory
    if (updatedTransfert.getStatus() == Transfert.TransfertStatus.COMPLETED) {
      TransfertHistory transfertHistory = new TransfertHistory();
      transfertHistory.setTransferId(updatedTransfert.getId());
      transfertHistory.setTransfertStatus(TransfertHistory.TransfertHistoryStatus.COMPLETED);
      transfertHistoryService.createTransfertHistory(transfertHistory);
    }

    return updatedTransfert;  }

  public void deleteTransfertById(Long id) {
    transfertRepository.deleteById(id);
  }


}

