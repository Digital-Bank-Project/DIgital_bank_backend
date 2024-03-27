package hei.school.digital.bank.repository;

import hei.school.digital.bank.DBConnection.PostgresDbConnection;
import hei.school.digital.bank.curdOperations.CrudOperations;
import hei.school.digital.bank.model.Transfert;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TransfertRepository implements CrudOperations<Transfert,Long> {

  @Override
  public Transfert create(Transfert entity) {
    try (Connection connection = PostgresDbConnection.getConnection()) {
      String sql = "INSERT INTO transfert (senderAccountId, recipientAccountId, amount, reason, effectiveDate, registrationDate, status, label) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

      statement.setLong(1, entity.getSenderAccountId());
      statement.setLong(2, entity.getRecipientAccountId());
      statement.setDouble(3, entity.getAmount());
      statement.setString(4, entity.getReason());
      statement.setTimestamp(5, Timestamp.valueOf(entity.getEffectiveDate()));
      statement.setTimestamp(6, Timestamp.valueOf(entity.getRegistrationDate()));
      statement.setString(7, entity.getStatus().toString());
      statement.setString(8,entity.getLabel());

      int affectedRows = statement.executeUpdate();

      if (affectedRows == 0) {
        throw new SQLException("Creating transfert failed, no rows affected.");
      }

      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          entity.setId(generatedKeys.getLong(1));
          return entity;
        } else {
          throw new SQLException("Creating transfert failed, no ID obtained.");
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  @Override
  public List<Transfert> findAll() {
    List<Transfert> transferts = new ArrayList<>();

    try (Connection connection = PostgresDbConnection.getConnection()) {
      String sql = "SELECT * FROM transfert";
      PreparedStatement statement = connection.prepareStatement(sql);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        Transfert transfert = new Transfert();
        transfert.setId(resultSet.getLong("id"));
        transfert.setSenderAccountId(resultSet.getLong("senderAccountId"));
        transfert.setRecipientAccountId(resultSet.getLong("recipientAccountId"));
        transfert.setAmount(resultSet.getDouble("amount"));
        transfert.setReason(resultSet.getString("reason"));
        transfert.setEffectiveDate(resultSet.getTimestamp("effectiveDate").toLocalDateTime());
        transfert.setRegistrationDate(resultSet.getTimestamp("registrationDate").toLocalDateTime());
        transfert.setStatus(Transfert.TransfertStatus.valueOf(resultSet.getString("status")));
        transfert.setLabel(resultSet.getString("label"));
        transferts.add(transfert);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return transferts;
  }

  @Override
  public Transfert findById(Long id) {
    Transfert transfert = null;

    try (Connection connection = PostgresDbConnection.getConnection()) {
      String sql = "SELECT * FROM transfert WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setLong(1, id);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        transfert = new Transfert();
        transfert.setId(resultSet.getLong("id"));
        transfert.setSenderAccountId(resultSet.getLong("senderAccountId"));
        transfert.setRecipientAccountId(resultSet.getLong("recipientAccountId"));
        transfert.setAmount(resultSet.getDouble("amount"));
        transfert.setReason(resultSet.getString("reason"));
        transfert.setEffectiveDate(resultSet.getTimestamp("effectiveDate").toLocalDateTime());
        transfert.setRegistrationDate(resultSet.getTimestamp("registrationDate").toLocalDateTime());
        transfert.setStatus(Transfert.TransfertStatus.valueOf(resultSet.getString("status")));
        transfert.setLabel(resultSet.getString("label"));
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return transfert;
  }

  @Override
  public Transfert update(Transfert entity) {
    try (Connection connection = PostgresDbConnection.getConnection()) {
      String sql = "UPDATE transfert SET senderAccountId = ?, recipientAccountId = ?, amount = ?, reason = ?, effectiveDate = ?, registrationDate = ?, status = ?, label = ? WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setLong(1, entity.getSenderAccountId());
      statement.setLong(2, entity.getRecipientAccountId());
      statement.setDouble(3, entity.getAmount());
      statement.setString(4, entity.getReason());
      statement.setTimestamp(5, Timestamp.valueOf(entity.getEffectiveDate()));
      statement.setTimestamp(6, Timestamp.valueOf(entity.getRegistrationDate()));
      statement.setString(7, entity.getStatus().toString());
      statement.setString(8, entity.getLabel());
      statement.setLong(9, entity.getId());

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
      String sql = "DELETE FROM transfert WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setLong(1, id);
      statement.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public List<Transfert> findTransfersByEffectiveDateBeforeAndStatus(LocalDateTime currentDate, Transfert.TransfertStatus status) {
    List<Transfert> transfers = new ArrayList<>();

    String sql = "SELECT * FROM transfert WHERE effectiveDate <= ? AND status = ?";
    try (Connection connection = PostgresDbConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setTimestamp(1, Timestamp.valueOf(currentDate));
      statement.setString(2, status.name());

      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        Transfert transfert = new Transfert();
        transfert.setId(resultSet.getLong("id"));
        transfert.setSenderAccountId(resultSet.getLong("senderAccountId"));
        transfert.setRecipientAccountId(resultSet.getLong("recipientAccountId"));
        transfert.setAmount(resultSet.getDouble("amount"));
        transfert.setReason(resultSet.getString("reason"));
        transfert.setEffectiveDate(resultSet.getTimestamp("effectiveDate").toLocalDateTime());
        transfert.setRegistrationDate(resultSet.getTimestamp("registrationDate").toLocalDateTime());
        transfert.setStatus(Transfert.TransfertStatus.valueOf(resultSet.getString("status")));
        transfert.setLabel(resultSet.getString("label"));
        transfers.add(transfert);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return transfers;
  }

  public List<Transfert> findTransfersBySenderAccountId(Long accountId) {
    List<Transfert> transferts = new ArrayList<>();

    try (Connection connection = PostgresDbConnection.getConnection()) {
      String sql = "SELECT * FROM transfert WHERE senderAccountId = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setLong(1, accountId);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        Transfert transfert = new Transfert();
        transfert.setId(resultSet.getLong("id"));
        transfert.setSenderAccountId(resultSet.getLong("senderAccountId"));
        transfert.setRecipientAccountId(resultSet.getLong("recipientAccountId"));
        transfert.setAmount(resultSet.getDouble("amount"));
        transfert.setReason(resultSet.getString("reason"));
        transfert.setEffectiveDate(resultSet.getTimestamp("effectiveDate").toLocalDateTime());
        transfert.setRegistrationDate(resultSet.getTimestamp("registrationDate").toLocalDateTime());
        transfert.setStatus(Transfert.TransfertStatus.valueOf(resultSet.getString("status")));
        transfert.setLabel(resultSet.getString("label"));
        transferts.add(transfert);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return transferts;
  }

}
