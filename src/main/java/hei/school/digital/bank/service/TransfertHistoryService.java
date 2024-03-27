package hei.school.digital.bank.service;

import hei.school.digital.bank.model.TransfertHistory;
import hei.school.digital.bank.repository.TransfertHistoryRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransfertHistoryService {

  private final TransfertHistoryRepository transfertHistoryRepository;

  public List<TransfertHistory> findAllTransfertHistories() {
    return transfertHistoryRepository.findAll();
  }

  public TransfertHistory findTransfertHistoryById(Long id) {
    return transfertHistoryRepository.findById(id);
  }

  public TransfertHistory createTransfertHistory(TransfertHistory transfertHistory) {
    return transfertHistoryRepository.create(transfertHistory);
  }

  public TransfertHistory updateTransfertHistory(TransfertHistory transfertHistory) {
    // Votre logique de mise à jour ici
    return transfertHistoryRepository.update(transfertHistory);
  }

  public void deleteTransfertHistoryById(Long id) {
    transfertHistoryRepository.deleteById(id);
  }

}
