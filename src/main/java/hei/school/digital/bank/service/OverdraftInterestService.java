package hei.school.digital.bank.service;

import hei.school.digital.bank.model.OverdraftInterest;
import hei.school.digital.bank.repository.OverdraftInterestRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OverdraftInterestService {

  private final OverdraftInterestRepository overdraftInterestRepository;

  public OverdraftInterest createOverdraftInterest(OverdraftInterest overdraftInterest) {
    return overdraftInterestRepository.create(overdraftInterest);
  }

  public List<OverdraftInterest> getAllOverdraftInterests() {
    return overdraftInterestRepository.findAll();
  }

  public OverdraftInterest getOverdraftInterestById(Long id) {
    return overdraftInterestRepository.findById(id);
  }

  public OverdraftInterest updateOverdraftInterest(OverdraftInterest overdraftInterest) {
    return overdraftInterestRepository.update(overdraftInterest);
  }

  public void deleteOverdraftInterestById(Long id) {
    overdraftInterestRepository.deleteById(id);
  }

}
