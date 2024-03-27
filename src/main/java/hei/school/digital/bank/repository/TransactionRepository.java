package hei.school.digital.bank.repository;

import hei.school.digital.bank.DBConnection.PostgresDbConnection;
import hei.school.digital.bank.curdOperations.CrudOperations;
import hei.school.digital.bank.model.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository implements CrudOperations<Transaction,Long> {

  @Override
  public Transaction create(Transaction transaction) {
    try (Connection connection = PostgresDbConnection.getConnection()) {
      String sql = "INSERT INTO transaction (amount, motive, dateTime, type, accountId) VALUES (?, ?, ?, ?, ?)";
      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      statement.setDouble(1, transaction.getAmount());
      statement.setString(2,transaction.getMotive());
      statement.setTimestamp(3, Timestamp.valueOf(transaction.getDateTime()));
      statement.setString(4, transaction.getType().toString());
      statement.setLong(5, transaction.getAccountId());

      int rowsInserted = statement.executeUpdate();

      if (rowsInserted > 0) {
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
          Long id = generatedKeys.getLong(1);
          transaction.setId(id);
        } else {
          throw new SQLException("Error retrieving generated ID.");
        }
      } else {
        throw new SQLException("Failure to insert the Transaction entity into the database.");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return transaction;
  }

  @Override
  public List<Transaction> findAll() {
    List<Transaction> transactions = new ArrayList<>();

    try (Connection connection = PostgresDbConnection.getConnection()) {
      String sql = "SELECT * FROM transaction";
      PreparedStatement statement = connection.prepareStatement(sql);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        Transaction transaction = new Transaction();
        transaction.setId(resultSet.getLong("id"));
        transaction.setAmount(resultSet.getDouble("amount"));
        transaction.setMotive(resultSet.getString("motive"));
        transaction.setDateTime(resultSet.getTimestamp("dateTime").toLocalDateTime());
        transaction.setType(Transaction.TransactionType.valueOf(resultSet.getString("type")));
        transaction.setAccountId(resultSet.getLong("accountId"));
        transactions.add(transaction);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return transactions;
  }


  @Override
  public Transaction findById(Long id) {
    Transaction transaction = null;
    try (Connection connection = PostgresDbConnection.getConnection()) {
      String sql = "SELECT * FROM transaction WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setLong(1, id);

      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        transaction = new Transaction();
        transaction.setId(resultSet.getLong("id"));
        transaction.setAmount(resultSet.getDouble("amount"));
        transaction.setMotive(resultSet.getString("motive"));
        transaction.setDateTime(resultSet.getTimestamp("date_time").toLocalDateTime());
        transaction.setType(Transaction.TransactionType.valueOf(resultSet.getString("type")));
        transaction.setAccountId(resultSet.getLong("account_id"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return transaction;
  }

  @Override
  public Transaction update(Transaction entity) {
    try (Connection connection = PostgresDbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
             "UPDATE transaction SET amount = ?, motive = ?, dateTime = ?, type = ?, accountId = ? WHERE id = ?")) {

      preparedStatement.setDouble(1, entity.getAmount());
      preparedStatement.setString(2,entity.getMotive());
      preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getDateTime()));
      preparedStatement.setString(4, entity.getType().toString());
      preparedStatement.setLong(5, entity.getAccountId());
      preparedStatement.setLong(6, entity.getId());

      int rowsUpdated = preparedStatement.executeUpdate();

      if (rowsUpdated > 0) {
        System.out.println("Transaction updated successfully.");
        return entity;
      } else {
        System.out.println("Failed to update transaction with ID " + entity.getId() + ".");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }



  @Override
  public void deleteById(Long id) {
    try (Connection connection = PostgresDbConnection.getConnection()) {
      String sql = "DELETE FROM transaction WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setLong(1, id);
      statement.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public List<Transaction> getTransactionsByDateRange(Date startDate, Date endDate) {
    List<Transaction> transactions = new ArrayList<>();

    try (Connection connection = PostgresDbConnection.getConnection()) {
      // Convertir la date en LocalDate
      Instant startInstant = startDate.toInstant();
      ZonedDateTime startZonedDateTime = startInstant.atZone(ZoneId.systemDefault());
      LocalDate localStartDate = startZonedDateTime.toLocalDate();

      Instant endInstant = endDate.toInstant();
      ZonedDateTime endZonedDateTime = endInstant.atZone(ZoneId.systemDefault());
      LocalDate localEndDate = endZonedDateTime.toLocalDate();

      // Convertir LocalDate en LocalDateTime avec minuit comme heure
      LocalDateTime startDateTime = localStartDate.atStartOfDay();
      LocalDateTime endDateTime = localEndDate.atTime(LocalTime.MAX);

      String sql = "SELECT * FROM transaction WHERE dateTime BETWEEN ? AND ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setTimestamp(1, Timestamp.valueOf(startDateTime));
      statement.setTimestamp(2, Timestamp.valueOf(endDateTime));

      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        Transaction transaction = new Transaction();
        transaction.setId(resultSet.getLong("id"));
        transaction.setAmount(resultSet.getDouble("amount"));
        transaction.setMotive(resultSet.getString("motive"));
        transaction.setDateTime(resultSet.getTimestamp("dateTime").toLocalDateTime());
        transaction.setType(Transaction.TransactionType.valueOf(resultSet.getString("type")));
        transaction.setAccountId(resultSet.getLong("accountId"));
        transactions.add(transaction);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return transactions;
  }
}
