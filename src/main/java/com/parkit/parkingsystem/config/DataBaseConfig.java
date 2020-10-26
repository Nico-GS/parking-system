package com.parkit.parkingsystem.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataBaseConfig {

    private static final Logger LOGGER = LogManager.getLogger("DataBaseConfig");
    private static final String SQL_PROPERTIES = "src/main/resources/SQL_properties.properties";
    private String url;
    private String username;
    private String password;

    /**
     * @return Connection to MySQL
     * @throws ClassNotFoundException nf
     * @throws SQLException           ie
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        LOGGER.debug("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (InputStream inputStream = new FileInputStream(SQL_PROPERTIES)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            url = properties.getProperty("url");
            username = properties.getProperty("userName");
            password = properties.getProperty("password");
        } catch (FileNotFoundException nf) {
            LOGGER.info("SQL Properties not found", nf);
        } catch (IOException ie) {
            LOGGER.info("Error SQL connection", ie);
        }
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * @param con Release database resources, close connection
     */
    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                LOGGER.debug("Closing DB connection");
            } catch (SQLException e) {
                LOGGER.error("Error while closing connection", e);
            }
        }
    }

    /**
     * @param ps Close PreparedStatement
     */
    public void closePreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                LOGGER.debug("Closing Prepared Statement");
            } catch (SQLException e) {
                LOGGER.error("Error while closing prepared statement", e);
            }
        }
    }

    /**
     * @param rs Close ResultSet
     */
    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                LOGGER.debug("Closing Result Set");
            } catch (SQLException e) {
                LOGGER.info("Error while closing result set", e);
            }
        }
    }
}
