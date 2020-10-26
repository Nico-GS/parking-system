package com.parkit.parkingsystem.integration.database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DataBasePrepareService {

    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    public void clearDataBaseEntries() {
        Connection connection = null;
        try {
            connection = dataBaseTestConfig.getConnection();
            connection.prepareStatement("update parking set available = true").execute(); // Stationnement disponible
            connection.prepareStatement("truncate table ticket").execute(); // Efface table tickets
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }
}
