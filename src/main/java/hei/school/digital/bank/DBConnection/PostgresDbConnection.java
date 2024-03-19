package hei.school.digital.bank.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDbConnection {

  public static Connection getConnection(){

    String url = System.getenv("DB_URL");
    String user = System.getenv("DB_USER");
    String password = System.getenv("DB_PASSWORD");

    Connection connection = null;

    try {
      Class.forName("org.postgresql.Driver");

      connection = DriverManager.getConnection(url, user, password);

    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }

    return connection;

  }

}
