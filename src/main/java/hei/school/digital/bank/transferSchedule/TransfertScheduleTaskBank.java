package hei.school.digital.bank.transferSchedule;

import hei.school.digital.bank.model.Account;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import hei.school.digital.bank.service.TransfertService;
import hei.school.digital.bank.model.Transfert;


@AllArgsConstructor
public class TransfertScheduleTaskBank implements Runnable{
  private TransfertService transferService;

  public void scheduleDeferredTransfer(Account senderAccount, Transfert transfer) {
    ScheduledExecutorService transferScheduler = Executors.newScheduledThreadPool(1);
    // Planifier le transfert différé après 48 heures
    transferScheduler.schedule(() -> transferService.performDeferredTransfer(senderAccount, transfer), 48, TimeUnit.HOURS);
  }

  @Override
  public void run() {
    transferService.processDeferredTransfers();
  }
}
