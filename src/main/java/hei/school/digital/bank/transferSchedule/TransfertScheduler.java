package hei.school.digital.bank.transferSchedule;

import hei.school.digital.bank.model.Transfert;
import hei.school.digital.bank.service.TransfertService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;

@AllArgsConstructor

public class TransfertScheduler implements Runnable{
  private final TransfertService transfertService;

  public void planifier(Transfert transfert,LocalDateTime dateEffet) {
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    LocalDateTime now = LocalDateTime.now();
    Duration duration = Duration.between(now, dateEffet);
    long delay = duration.getSeconds();
    if (delay < 0) {
      delay = 0;
    }
    scheduler.schedule(() -> transfertService.planifierTransfertTask(transfert), delay,
        TimeUnit.SECONDS);
  }

  @Override
  public void run() {
    transfertService.processPlanifierTask();
  }

}
