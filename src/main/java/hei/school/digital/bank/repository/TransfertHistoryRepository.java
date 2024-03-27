package hei.school.digital.bank.repository;

import hei.school.digital.bank.DBConnection.PostgresDbConnection;
import hei.school.digital.bank.curdOperations.CrudOperations;
import hei.school.digital.bank.model.TransfertHistory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransfertHistoryRepository implements CrudOperations<TransfertHistory,Long> {
  @Override
  public TransfertHistory create(TransfertHistory entity) {
    try (Connection connection = PostgresDbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
             "INSERT INTO transfertHistory (transfertId, transfertStatus) VALUES (?, ?)")) {

      preparedStatement.setLong(1, entity.getTransferId());
      preparedStatement.setString(2, entity.getTransfertStatus().toString());

      int rowsInserted = preparedStatement.executeUpdate();

      if (rowsInserted > 0) {
        System.out.println("Transfert history created successfully.");
        return entity;
      } else {
        System.out.println("Failed to create Transfert history.");
        return null;
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  @Override
  public List<TransfertHistory> findAll() {
    List<TransfertHistory> transfertHistories = new ArrayList<>();

    try (Connection connection = PostgresDbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM transfertHistory");
         ResultSet resultSet = preparedStatement.executeQuery()) {

      while (resultSet.next()) {
        TransfertHistory transfertHistory = new TransfertHistory();
        transfertHistory.setId(resultSet.getLong("id"));
        transfertHistory.setTransferId(resultSet.getLong("transfertId"));
        transfertHistory.setTransfertStatus(TransfertHistory.TransfertHistoryStatus.valueOf(resultSet.getString("transfertStatus").toString()));
        transfertHistories.add(transfertHistory);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return transfertHistories;
  }


  @Override
  public TransfertHistory findById(Long id) {
    TransfertHistory transfertHistory = null;
    try (Connection connection = PostgresDbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM transfertHistory WHERE id = ?")) {
      preparedStatement.setLong(1, id);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          transfertHistory = new TransfertHistory();
          transfertHistory.setId(resultSet.getLong("id"));
          transfertHistory.setTransferId(resultSet.getLong("transfertId"));
          transfertHistory.setTransfertStatus(TransfertHistory.TransfertHistoryStatus.valueOf(resultSet.getString("transfertStatus")));
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return transfertHistory;
  }

  @Override
  public TransfertHistory update(TransfertHistory entity) {
    TransfertHistory updatedTransfertHistory = null;
    try (Connection connection = PostgresDbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("UPDATE transfertHistory SET transfertId = ?, transfertStatus = ? WHERE id = ?")) {
      preparedStatement.setLong(1, entity.getTransferId());
      preparedStatement.setString(2, entity.getTransfertStatus().toString());
      preparedStatement.setLong(3, entity.getId());
      int affectedRows = preparedStatement.executeUpdate();
      if (affectedRows > 0) {
        updatedTransfertHistory = entity;
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return updatedTransfertHistory;
  }

  @Override
  public void deleteById(Long id) {
    try (Connection connection = PostgresDbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM transfertHistory WHERE id = ?")) {
      preparedStatement.setLong(1, id);
      preparedStatement.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

}
