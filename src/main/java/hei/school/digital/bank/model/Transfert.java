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
@Table(name = "transfert")
public class Transfert {
  @Id
  private Long id;
  private Long senderAccountId;
  private Long recipientAccountId;
  private double amount;
  private String reason;
  private LocalDateTime effectiveDate;
  private LocalDateTime registrationDate;
  private TransfertStatus status;
  private String label;

  public enum TransfertStatus {
    PENDING,
    COMPLETED,
    CANCELLED,
    COMPLETED_PENDING
  }

}
