package com.parkit.parkingsystem.unit;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;
import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DataBaseConfigTest {

   private static DataBaseConfig dataBaseConfig;

   @BeforeEach
   public void setUpPerTest() {
      dataBaseConfig = new DataBaseConfig();
   }

   @Test
   @DisplayName("Connection")
   public void testConnection() throws ClassNotFoundException, SQLException {
      Connection con;
      con = dataBaseConfig.getConnection();
      assertThat(con).isNotNull();
   }

   @Test
   @DisplayName("Connection closed")
   public void closedConnection() throws ClassNotFoundException, SQLException {
      Connection con;
      con = dataBaseConfig.getConnection();
      dataBaseConfig.closeConnection(con);
      assertThat(con.isClosed()).isTrue();
   }

   @Test
   @DisplayName("Connection null")
   public void nullConnection() throws ClassNotFoundException, SQLException {
      Connection con = null;
      dataBaseConfig.getConnection();
      assertThat(con).isNull();
   }

   @Test
   @DisplayName("Close PreparedStatement")
   public void closePreparedStatement() throws ClassNotFoundException, SQLException {
      Connection con;
      con = dataBaseConfig.getConnection();
      PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
      dataBaseConfig.closePreparedStatement(ps);
      assertThat(ps.isClosed()).isTrue();
   }

   @Test
   @DisplayName("Null PreparedStatement")
   public void nullPreparedStatement() throws ClassNotFoundException, SQLException {
      PreparedStatement ps = null;
      dataBaseConfig.getConnection();
      assertThat(ps).isNull();
   }

   @Test
   @DisplayName("Closed ResultSet")
   public void closedResultSet() throws ClassNotFoundException, SQLException {
      Connection con;
      con = dataBaseConfig.getConnection();
      ResultSet rs = null;
      PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
      ps.setString(1, "CAR");
      rs = ps.executeQuery();
      dataBaseConfig.closeResultSet(rs);
      assertThat(rs.isClosed()).isTrue();
   }

   @Test
   @DisplayName("Null ResultSet")
   public void nullResultSet() throws ClassNotFoundException, SQLException {
      ResultSet rs = null;
      dataBaseConfig.getConnection();
      assertThat(rs).isNull();
   }
}
