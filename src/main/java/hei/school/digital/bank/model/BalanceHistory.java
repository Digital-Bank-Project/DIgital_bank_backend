package hei.school.digital.bank.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "balanceHistory")
public class BalanceHistory {

  @Id
  private Long id;

  private Long accountId;

  private double principalBalance;

  private double loanAmount;

  private double interestAmount;

  private LocalDateTime timestamp;

}
