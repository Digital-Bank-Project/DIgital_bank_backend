package hei.school.digital.bank.repository;

import hei.school.digital.bank.DBConnection.PostgresDbConnection;
import hei.school.digital.bank.curdOperations.CrudOperations;
import hei.school.digital.bank.model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository implements CrudOperations<Account,Long> {
  @Override
  public Account create(Account entity) {
    try {
      Connection connection = PostgresDbConnection.getConnection();

      String sql = "INSERT INTO account (firstName, lastName, dateOfBirth, principalBalance, monthlySalary, accountNumber, accountStatus) " +
          "VALUES (?, ?, ?, ?, ?, ?, ?)";

      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, entity.getFirstName());
      statement.setString(2, entity.getLastName());
      statement.setDate(3, new java.sql.Date(entity.getDateOfBirth().getTime()));
      statement.setDouble(4, entity.getPrincipalBalance());
      statement.setDouble(5, entity.getMonthlySalary());
      statement.setString(6, entity.getAccountNumber());
      statement.setString(7, entity.getAccountStatus().toString());

      int rowsInserted = statement.executeUpdate();

      if (rowsInserted > 0) {
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
          Long id = generatedKeys.getLong(1);
          entity.setId(id);
        } else {
          throw new SQLException("Error retrieving generated ID.");
        }
      } else {
        throw new SQLException("Failure to insert the Account entity into the database.");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return entity;
  }

  @Override
  public List<Account> findAll() {
    List<Account> accounts = new ArrayList<>();

    try (Connection connection = PostgresDbConnection.getConnection()) {
      String sql = "SELECT * FROM account";
      PreparedStatement statement = connection.prepareStatement(sql);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        Account account = new Account();
        account.setId(resultSet.getLong("id"));
        account.setFirstName(resultSet.getString("firstName"));
        account.setLastName(resultSet.getString("lastName"));
        account.setDateOfBirth(resultSet.getDate("dateOfBirth"));
        account.setPrincipalBalance(resultSet.getDouble("principalBalance"));
        account.setMonthlySalary(resultSet.getDouble("monthlySalary"));
        account.setAccountNumber(resultSet.getString("accountNumber"));
        account.setAccountStatus(Account.AccountStatus.valueOf(resultSet.getString("accountStatus")));        // Assurez-vous de récupérer les autres attributs de l'Account de manière similaire
        accounts.add(account);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return accounts;
  }


  @Override
  public Account findById(Long id) {
    Account account = null;

    try {
      Connection connection = PostgresDbConnection.getConnection();

      String sql = "SELECT * FROM account WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setLong(1, id);

      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        account = new Account();
        account.setId(resultSet.getLong("id"));
        account.setFirstName(resultSet.getString("firstName"));
        account.setLastName(resultSet.getString("lastName"));
        account.setDateOfBirth(resultSet.getDate("dateOfBirth"));
        account.setPrincipalBalance(resultSet.getDouble("principalBalance"));
        account.setMonthlySalary(resultSet.getDouble("monthlySalary"));
        account.setAccountNumber(resultSet.getString("accountNumber"));
        account.setAccountStatus(Account.AccountStatus.valueOf(resultSet.getString("accountStatus")));
      } else {
        System.out.println("The account with the ID "+ id +" does not exist.");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return account;
  }


  @Override
  public Account update(Account entity) {
    try (Connection connection = PostgresDbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
             "UPDATE account SET firstName = ?, lastName = ?, dateOfBirth = ?, principalBalance = ?, monthlySalary = ?, accountNumber = ?, accountStatus = ? WHERE id = ?")) {

      preparedStatement.setString(1, entity.getFirstName());
      preparedStatement.setString(2, entity.getLastName());
      preparedStatement.setDate(3, new java.sql.Date(entity.getDateOfBirth().getTime()));
      preparedStatement.setDouble(4, entity.getPrincipalBalance());
      preparedStatement.setDouble(5, entity.getMonthlySalary());
      preparedStatement.setString(6, entity.getAccountNumber());
      preparedStatement.setString(7, entity.getAccountStatus().toString());
      preparedStatement.setLong(8, entity.getId());

      int rowsUpdated = preparedStatement.executeUpdate();

      if (rowsUpdated > 0) {
        System.out.println("Account updated successfully.");
        return entity;
      } else {
        System.out.println("Failed to update account with ID " + entity.getId() + ".");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }



  @Override
  public void deleteById(Long id) {
    try {
      Connection connection = PostgresDbConnection.getConnection();

      String sql = "DELETE FROM account WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setLong(1, id);

      int rowsDeleted = statement.executeUpdate();

      if (rowsDeleted > 0) {
        System.out.println("The account with the ID "+ id +" has been successfully deleted.");
      } else {
        System.out.println("The account with the ID "+ id +" does not exist.");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

}
