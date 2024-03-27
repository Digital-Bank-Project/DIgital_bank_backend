package hei.school.digital.bank.service;

import hei.school.digital.bank.model.BalanceHistory;
import hei.school.digital.bank.repository.BalanceHistoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BalanceHistoryService {

  private final BalanceHistoryRepository balanceHistoryRepository;

  public BalanceHistory createBalanceHistory(BalanceHistory balanceHistory) {
    return balanceHistoryRepository.create(balanceHistory);
  }

  public List<BalanceHistory> getAllBalanceHistory() {
    return balanceHistoryRepository.findAll();
  }

  public BalanceHistory getBalanceHistoryById(Long id) {
    return balanceHistoryRepository.findById(id);
  }

  public BalanceHistory updateBalanceHistory(BalanceHistory balanceHistory) {
    return balanceHistoryRepository.update(balanceHistory);
  }

  public void deleteBalanceHistoryById(Long id) {
    balanceHistoryRepository.deleteById(id);
  }

  public List<BalanceHistory> findByAccountId(Long accountId) {
    return balanceHistoryRepository.findByAccountId(accountId);
  }



}
