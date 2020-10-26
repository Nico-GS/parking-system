package com.parkit.parkingsystem.integration.database;


import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * DataBase prepare service
 */
public class DataBasePrepareService {

    /**
     * The Data base test config.
     */
    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    /**
     * Clear data base entries
     */
    public void clearDataBaseEntries() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = dataBaseTestConfig.getConnection();
            connection.prepareStatement("update parking set available = true").execute(); // Stationnement disponible
            connection.prepareStatement("truncate table ticket").execute(); // Efface table tickets
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataBaseTestConfig.closeConnection(connection);
            dataBaseTestConfig.closePreparedStatement(ps);
        }
    }
}
