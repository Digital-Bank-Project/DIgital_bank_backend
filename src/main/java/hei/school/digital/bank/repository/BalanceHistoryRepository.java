package hei.school.digital.bank.repository;

import hei.school.digital.bank.DBConnection.PostgresDbConnection;
import hei.school.digital.bank.curdOperations.CrudOperations;
import hei.school.digital.bank.model.BalanceHistory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class BalanceHistoryRepository implements CrudOperations<BalanceHistory,Long> {
  @Override
  public BalanceHistory create(BalanceHistory entity) {
    try (Connection connection = PostgresDbConnection.getConnection()) {
      String sql = "INSERT INTO balanceHistory (accountId, principalBalance, loanAmount, interestAmount, timestamp) VALUES (?, ?, ?, ?, ?)";
      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

      statement.setLong(1, entity.getAccountId());
      statement.setDouble(2, entity.getPrincipalBalance());
      statement.setDouble(3, entity.getLoanAmount());
      statement.setDouble(4, entity.getInterestAmount());
      statement.setTimestamp(5, Timestamp.valueOf(entity.getTimestamp()));

      int affectedRows = statement.executeUpdate();

      if (affectedRows == 0) {
        throw new SQLException("Creating balance history failed, no rows affected.");
      }

      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          entity.setId(generatedKeys.getLong(1));
          return entity;
        } else {
          throw new SQLException("Creating balance history failed, no ID obtained.");
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }


  @Override
  public List<BalanceHistory> findAll() {
    List<BalanceHistory> balanceHistories = new ArrayList<>();

    try (Connection connection = PostgresDbConnection.getConnection()) {
      String sql = "SELECT * FROM balanceHistory";
      PreparedStatement statement = connection.prepareStatement(sql);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        BalanceHistory balanceHistory = new BalanceHistory();
        balanceHistory.setId(resultSet.getLong("id"));
        balanceHistory.setAccountId(resultSet.getLong("accountId"));
        balanceHistory.setPrincipalBalance(resultSet.getDouble("principalBalance"));
        balanceHistory.setLoanAmount(resultSet.getDouble("loanAmount"));
        balanceHistory.setInterestAmount(resultSet.getDouble("interestAmount"));
        balanceHistory.setTimestamp(resultSet.getTimestamp("timestamp").toLocalDateTime());
        balanceHistories.add(balanceHistory);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return balanceHistories;
  }

  @Override
  public BalanceHistory findById(Long id) {
    BalanceHistory balanceHistory = null;

    try (Connection connection = PostgresDbConnection.getConnection()) {
      String sql = "SELECT * FROM balanceHistory WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setLong(1, id);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        balanceHistory = new BalanceHistory();
        balanceHistory.setId(resultSet.getLong("id"));
        balanceHistory.setAccountId(resultSet.getLong("accountId"));
        balanceHistory.setPrincipalBalance(resultSet.getDouble("principalBalance"));
        balanceHistory.setLoanAmount(resultSet.getDouble("loanAmount"));
        balanceHistory.setInterestAmount(resultSet.getDouble("interestAmount"));
        balanceHistory.setTimestamp(resultSet.getTimestamp("timestamp").toLocalDateTime());
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return balanceHistory;
  }


  @Override
  public BalanceHistory update(BalanceHistory entity) {
    try (Connection connection = PostgresDbConnection.getConnection()) {
      String sql = "UPDATE balanceHistory SET accountId = ?, principalBalance = ?, loanAmount = ?, interestAmount = ?, timestamp = ? WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setLong(1, entity.getAccountId());
      statement.setDouble(2, entity.getPrincipalBalance());
      statement.setDouble(3, entity.getLoanAmount());
      statement.setDouble(4, entity.getInterestAmount());
      statement.setTimestamp(5, Timestamp.valueOf(entity.getTimestamp()));
      statement.setLong(6, entity.getId());

      int rowsUpdated = statement.executeUpdate();

      if (rowsUpdated > 0) {
        return entity;
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return null;
  }


  @Override
  public void deleteById(Long id) {
    try (Connection connection = PostgresDbConnection.getConnection()) {
      String sql = "DELETE FROM balanceHistory WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setLong(1, id);
      statement.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }



}
