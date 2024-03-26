package hei.school.digital.bank.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accountStatement")
public class AccountStatement {

  private Long accountId;
  private Date effectiveDate;
  private String motive;
  private Double principalBalance;
  private Double creditMga;
  private Double debitMga;
  private String ref;

}
