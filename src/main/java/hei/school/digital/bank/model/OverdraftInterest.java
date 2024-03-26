package hei.school.digital.bank.model;

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
@Table(name = "overdraftInterest")
public class OverdraftInterest {
  @Id
  private Long id;
  private double interestRateForFirst7Days;
  private double interestRateAfter7Days;

}

