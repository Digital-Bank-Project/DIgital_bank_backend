package hei.school.digital.bank.model;

import java.sql.Timestamp;
import java.util.Date;
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
@Table(name = "account")
public class Account {
  @Id
  private Long id;
  private String firstName;
  private String lastName;
  private Date dateOfBirth;
  private double principalBalance;
  private double monthlySalary;
  private String accountNumber;
  private boolean overdraftEnabled;
  private double overdraftInterest;
  private OverdraftInterest interestDetails;
  private Timestamp overdraftStartDate;
  private Bank bank;

  public enum Bank {
    BMOI,
    BNI,
    BFV
  }

}
