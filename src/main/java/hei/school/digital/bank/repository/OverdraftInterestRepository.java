package hei.school.digital.bank.repository;

import hei.school.digital.bank.DBConnection.PostgresDbConnection;
import hei.school.digital.bank.curdOperations.CrudOperations;
import hei.school.digital.bank.model.OverdraftInterest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OverdraftInterestRepository implements CrudOperations<OverdraftInterest,Long> {
  @Override
  public OverdraftInterest create(OverdraftInterest entity) {
    try (Connection connection = PostgresDbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
             "INSERT INTO overdraftInterest (interestRateForFirst7Days, interestRateAfter7Days) VALUES (?, ?)")) {

      preparedStatement.setDouble(1, entity.getInterestRateForFirst7Days());
      preparedStatement.setDouble(2, entity.getInterestRateAfter7Days());

      int rowsInserted = preparedStatement.executeUpdate();

      if (rowsInserted > 0) {
        System.out.println("Overdraft interest created successfully.");
        return entity;
      } else {
        System.out.println("Failed to create overdraft interest.");
        return null;
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  @Override
  public List<OverdraftInterest> findAll() {
    List<OverdraftInterest> overdraftInterests = new ArrayList<>();

    try (Connection connection = PostgresDbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM overdraftInterest");
         ResultSet resultSet = preparedStatement.executeQuery()) {

      while (resultSet.next()) {
        OverdraftInterest overdraftInterest = new OverdraftInterest();
        overdraftInterest.setId(resultSet.getLong("id"));
        overdraftInterest.setInterestRateForFirst7Days(resultSet.getDouble("interestRateForFirst7Days"));
        overdraftInterest.setInterestRateAfter7Days(resultSet.getDouble("interestRateAfter7Days"));
        overdraftInterests.add(overdraftInterest);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return overdraftInterests;
  }

  @Override
  public OverdraftInterest findById(Long id) {
    OverdraftInterest overdraftInterest = null;

    try (Connection connection = PostgresDbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM overdraftInterest WHERE id = ?")) {

      preparedStatement.setLong(1, id);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          overdraftInterest = new OverdraftInterest();
          overdraftInterest.setId(resultSet.getLong("id"));
          overdraftInterest.setInterestRateForFirst7Days(resultSet.getDouble("interestRateForFirst7Days"));
          overdraftInterest.setInterestRateAfter7Days(resultSet.getDouble("interestRateAfter7Days"));
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return overdraftInterest;
  }

  @Override
  public OverdraftInterest update(OverdraftInterest entity) {
    try (Connection connection = PostgresDbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
             "UPDATE overdraftInterest SET interestRateForFirst7Days = ?, interestRateAfter7Days = ? WHERE id = ?")) {

      preparedStatement.setDouble(1, entity.getInterestRateForFirst7Days());
      preparedStatement.setDouble(2, entity.getInterestRateAfter7Days());
      preparedStatement.setLong(3, entity.getId());

      int rowsUpdated = preparedStatement.executeUpdate();

      if (rowsUpdated > 0) {
        System.out.println("Overdraft interest updated successfully.");
        return entity;
      } else {
        System.out.println("Failed to update overdraft interest.");
        return null;
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  @Override
  public void deleteById(Long id) {
    try (Connection connection = PostgresDbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM overdraftInterest WHERE id = ?")) {

      preparedStatement.setLong(1, id);
      int rowsDeleted = preparedStatement.executeUpdate();

      if (rowsDeleted > 0) {
        System.out.println("Overdraft interest deleted successfully.");
      } else {
        System.out.println("Failed to delete overdraft interest.");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

}
