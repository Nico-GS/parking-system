package com.parkit.parkingsystem.integration.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.parkit.parkingsystem.config.DataBaseConfig;

public class DataBaseTestConfig extends DataBaseConfig {

    private static final Logger LOGGER = LogManager.getLogger("DataBaseConfig");
    private static final String SQL_PROPERTIES = "src/main/resources/SQL_properties.properties";
    private String url;
    private String username;
    private String password;

    public Connection getConnection() throws ClassNotFoundException, SQLException {
	LOGGER.info("Create DB connection");
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

    public void closeConnection(Connection con) {
	if (con != null) {
	    try {
		con.close();
		LOGGER.info("Closing DB connection");
	    } catch (SQLException e) {
		LOGGER.error("Error while closing connection", e);
	    }
	}
    }

    public void closePreparedStatement(PreparedStatement ps) {
	if (ps != null) {
	    try {
		ps.close();
		LOGGER.info("Closing Prepared Statement");
	    } catch (SQLException e) {
		LOGGER.error("Error while closing prepared statement", e);
	    }
	}
    }

    public void closeResultSet(ResultSet rs) {
	if (rs != null) {
	    try {
		rs.close();
		LOGGER.info("Closing Result Set");
	    } catch (SQLException e) {
		LOGGER.info("Error while closing result set", e);
	    }
	}
    }
}
