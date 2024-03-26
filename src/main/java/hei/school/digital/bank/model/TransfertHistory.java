package hei.school.digital.bank.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transfertHistory")
public class TransfertHistory {

  private Long id;
  private Long transferId;
  private TransfertHistoryStatus transfertStatus;

  public enum TransfertHistoryStatus{
    COMPLETED,CANCELlED
  }

}
