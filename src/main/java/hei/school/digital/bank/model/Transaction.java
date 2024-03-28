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
@Table(name = "transaction")
public class Transaction {

  @Id
  private Long id;
  private double amount;
  private String motive;
  private LocalDateTime dateTime;
  private TransactionType type;
  private Long accountId;
  private String category;

  public enum TransactionType {
    DEBIT, CREDIT
  }

}
