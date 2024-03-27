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

  public void manageTransfer(Transfert transfert) throws InsufficientFundsException {
    if (transfert == null) {
      throw new IllegalArgumentException("Transfer object cannot be null");
    }

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime dateEffet = transfert.getEffectiveDate();

    // Vérifier si la date d'effet du transfert est ultérieure à la date actuelle
    if (now.isBefore(dateEffet)) {
      // La date d'effet n'est pas encore arrivée, enregistrer le transfert avec le statut "PENDING"
      transfert.setStatus(Transfert.TransfertStatus.PENDING);
      updateTransfert(transfert);

      // Planifier le transfert pour la date d'effet spécifiée
      planifierTransfert(transfert, dateEffet);
    } else {
      // La date d'effet est passée ou égale à la date actuelle, traiter le transfert immédiatement
      List<Long> recipientAccountIds = Collections.singletonList(transfert.getRecipientAccountId());
      if (recipientAccountIds.size() == 1) {
        // Il n'y a qu'un seul compte destinataire
        processPendingTransfer(transfert);
      } else {
        // Plusieurs comptes destinataires
        processMultipleTransfers(transfert.getSenderAccountId(), recipientAccountIds, transfert.getAmount());
      }
    }
  }


  public void processPendingTransfer(Transfert transfer) throws InsufficientFundsException {
    if (transfer != null) {
      Account senderAccount = accountService.getAccountById(transfer.getSenderAccountId());
      Account recipientAccount = accountService.getAccountById(transfer.getRecipientAccountId());

      if (senderAccount != null && recipientAccount != null) {
        // Vérifier si le solde du compte expéditeur est suffisant pour le transfert
        if (senderAccount.getPrincipalBalance() >= transfer.getAmount()) {
          // Si les comptes sont de la même banque, effectuer le transfert instantanément
          if (senderAccount.getBank() == recipientAccount.getBank()) {
            performTransfer(senderAccount, recipientAccount, transfer);
          } else {
            // Si les comptes sont de banques différentes, différer le crédit du compte destinataire de 48 heures
            transfer.setStatus(Transfert.TransfertStatus.COMPLETED_PENDING);
            updateTransfert(transfer);
            if (transfer.getStatus() == Transfert.TransfertStatus.COMPLETED_PENDING) {
              transfer.setEffectiveDate(LocalDateTime.now());
              updateTransfert(transfer);
            }
            deferTransfer(senderAccount, transfer);
          }
        } else {
          // Si le solde du compte expéditeur est insuffisant, lancer une exception InsufficientFundsException
          throw new InsufficientFundsException("Insufficient funds in sender's account");
        }
      }
    }
  }

  private void performTransfer(Account senderAccount, Account recipientAccount, Transfert transfer) {
    // Débiter le montant du compte expéditeur
    senderAccount.setPrincipalBalance(senderAccount.getPrincipalBalance() - transfer.getAmount());
    accountService.updateAccount(senderAccount);

    // Créditer le montant au compte destinataire
    recipientAccount.setPrincipalBalance(recipientAccount.getPrincipalBalance() + transfer.getAmount());
    accountService.updateAccount(recipientAccount);

    // Mettre à jour le statut du transfert
    transfer.setEffectiveDate(LocalDateTime.now());
    transfer.setRegistrationDate(LocalDateTime.now());
    transfer.setStatus(Transfert.TransfertStatus.COMPLETED);
    updateTransfert(transfer);

    // Créer les transactions correspondantes
    createTransactions(senderAccount.getId(), recipientAccount.getId(), transfer.getAmount(), transfer.getReason());
  }

  // Dans deferTransfer
  private void deferTransfer(Account senderAccount, Transfert transfer) {

    // Débiter le montant du compte expéditeur immédiatement
    senderAccount.setPrincipalBalance(senderAccount.getPrincipalBalance() - transfer.getAmount());
    accountService.updateAccount(senderAccount);

    // Planifier la tâche de transfert différé après 48 heures
    transfertScheduleTask.scheduleDeferredTransfer(senderAccount, transfer);

  }

  public void processDeferredTransfers() {
    List<Transfert> pendingTransfers = getCompletPendingTransfers();

    for (Transfert transfer : pendingTransfers) {
      LocalDateTime now = LocalDateTime.now();
      LocalDateTime effectiveDate = transfer.getEffectiveDate();

      // Vérifier si la date d'exécution prévue est passée depuis plus de 48 heures
      if (effectiveDate.plusHours(48).isBefore(now)) {
        Account senderAccount = accountService.getAccountById(transfer.getSenderAccountId());
        if (senderAccount != null) {
          performDeferredTransfer(senderAccount, transfer);
        }
      }
    }
  }

  public List<Transfert> getCompletPendingTransfers() {
    LocalDateTime now = LocalDateTime.now();
    return transfertRepository.findTransfersByEffectiveDateBeforeAndStatus(now, Transfert.TransfertStatus.COMPLETED_PENDING);
  }

  // Dans la méthode performDeferredTransfer
  public void performDeferredTransfer(Account senderAccount, Transfert transfer) {
    Account recipientAccount = accountService.getAccountById(transfer.getRecipientAccountId());
    if (recipientAccount != null) {
      // Créditer le montant au compte destinataire
      recipientAccount.setPrincipalBalance(recipientAccount.getPrincipalBalance() + transfer.getAmount());
      accountService.updateAccount(recipientAccount);

      // Créer les transactions correspondantes
      createTransactions(senderAccount.getId(), recipientAccount.getId(), transfer.getAmount(), transfer.getReason());

      // Mettre à jour le statut du transfert
      transfer.setStatus(Transfert.TransfertStatus.COMPLETED);
      updateTransfert(transfer);
    }
  }

  private void createTransactions(Long senderAccountId, Long recipientAccountId, double amount, String motive) {
    // Créer une transaction de type Débit pour le compte expéditeur
    Transaction debitTransaction = new Transaction();
    debitTransaction.setAccountId(senderAccountId);
    debitTransaction.setAmount(amount);
    debitTransaction.setMotive(motive);
    debitTransaction.setType(Transaction.TransactionType.DEBIT);
    debitTransaction.setDateTime(LocalDateTime.now());
    transactionService.createTransaction(debitTransaction);

    // Créer une transaction de type Crédit pour le compte destinataire
    Transaction creditTransaction = new Transaction();
    creditTransaction.setAccountId(recipientAccountId);
    creditTransaction.setAmount(amount);
    debitTransaction.setMotive(motive);
    creditTransaction.setType(Transaction.TransactionType.CREDIT);
    creditTransaction.setDateTime(LocalDateTime.now());
    transactionService.createTransaction(creditTransaction);
  }

  public void processMultipleTransfers(Long senderAccountId, List<Long> recipientAccountIds, double amount) {
    // Check if the sender's balance can cover the total amount
    Account sender = accountService.getAccountById(senderAccountId);
    if (sender == null || sender.getPrincipalBalance() < amount * recipientAccountIds.size()) {
      // Handle case where balance is insufficient
      throw new InsufficientFundsException("Sender's balance is insufficient to process the transfers.");
    }

    // Create a list of transfers
    List<Transfert> transfers = new ArrayList<>();
    for (Long recipientId : recipientAccountIds) {
      Transfert transfer = new Transfert();
      transfer.setSenderAccountId(senderAccountId);
      transfer.setRecipientAccountId(recipientId);
      transfer.setAmount(amount);
      // Other transfer attributes and label if needed
      transfers.add(transfer);
    }

    // Process each transfer individually
    for (Transfert transfer : transfers) {
      processPendingTransfer(transfer);
    }
  }

  public void processPlanifierTask() {
    List<Transfert> pendingTransfers = getPendingTransfers();

    for (Transfert transfer : pendingTransfers) {
      LocalDateTime now = LocalDateTime.now();
      LocalDateTime effectiveDate = transfer.getEffectiveDate();

      // Vérifier si la date d'exécution prévue est passée
      if (effectiveDate.isBefore(now)) {
        planifierTransfertTask(transfer);
      }
    }
  }

  public List<Transfert> getPendingTransfers() {
    LocalDateTime now = LocalDateTime.now();
    return transfertRepository.findTransfersByEffectiveDateBeforeAndStatus(now, Transfert.TransfertStatus.PENDING);
  }

  public void planifierTransfert(Transfert transfert, LocalDateTime dateEffet) {
    transfertScheduler.planifier(transfert,dateEffet);
  }

  public void planifierTransfertTask(Transfert transfert) {
    processPendingTransfer(transfert);
  }

  public void cancelTransfer(Transfert transfer) {
    // Vérifier si le transfert existe
    if (transfer == null) {
      throw new IllegalArgumentException("Transfer object cannot be null");
    }

    // Vérifier si le statut du transfert est "Pending"
    if (transfer.getStatus() == Transfert.TransfertStatus.PENDING) {
      // Mettre à jour le statut du transfert en "Canceled"
      transfer.setStatus(Transfert.TransfertStatus.CANCELLED);
      updateTransfert(transfer);


      // Supprimer le transfert de la base de données si nécessaire
      deleteTransfertById(transfer.getId());

      TransfertHistory transfertHistory = new TransfertHistory();
      transfertHistory.setTransferId(transfer.getId());
      transfertHistory.setTransfertStatus(TransfertHistory.TransfertHistoryStatus.CANCELlED);
      transfertHistoryService.createTransfertHistory(transfertHistory);
    } else {
      // Le transfert ne peut être annulé que s'il est en attente (Pending)
      throw new IllegalStateException("Transfer cannot be canceled because it's not in pending state.");
    }
  }

  public List<Transfert> findTransfersBySenderAccountId(Long accountId) {
    return transfertRepository.findTransfersBySenderAccountId(accountId);
  }

}

