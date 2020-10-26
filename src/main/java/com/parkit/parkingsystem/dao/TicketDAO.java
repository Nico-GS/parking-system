package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

/**
 * Interact with database
 */
public class TicketDAO {

    private static final Logger LOGGER = LogManager.getLogger("TicketDAO");
    private DataBaseConfig dataBaseConfig = new DataBaseConfig();
    private static final String ERROR_MESSAGE = "Error fetching next available slot";
    private static final int SIX = 6;

    public void setDataBaseConfig(final DataBaseConfig dbConfig) {
        this.dataBaseConfig = dbConfig;
    }

    /**
     * Save ticket to database
     *
     * @param ticket save
     * @return true : saved | false : failed
     */
    public boolean saveTicket(final Ticket ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        int i = 1;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            ps.setInt(i, ticket.getParkingSpot().getId());
            i++;
            ps.setString(i, ticket.getVehicleRegNumber());
            i++;
            ps.setDouble(i, ticket.getPrice());
            i++;
            ps.setTimestamp(i, Timestamp.valueOf(ticket.getInTime()));
            i++;
            ps.setTimestamp(i, (ticket.getOutTime() == null) ? null : (Timestamp.valueOf(ticket.getOutTime())));
            return ps.execute();
        } catch (Exception ex) {
            LOGGER.error(ERROR_MESSAGE, ex);
        } finally {
            dataBaseConfig.closeConnection(con);
            dataBaseConfig.closePreparedStatement(ps);
        }
        return false;
    }

    /**
     * Get a ticket
     *
     * @param vehicleRegNumber registration number
     * @return the latest ticket found in database
     */
    public Ticket getTicket(final String vehicleRegNumber) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Ticket ticket = null;
        ParkingSpot parkingSpot;
        int i = 1;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.GET_TICKET);
            ps.setString(1, vehicleRegNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                ticket = new Ticket();
                parkingSpot = new ParkingSpot(rs.getInt(i), ParkingType.valueOf(rs.getString(SIX)), false);
                i++;
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(i));
                i++;
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(i));
                i++;
                ticket.setInTime(rs.getTimestamp(i).toLocalDateTime());
                i++;
                ticket.setOutTime((rs.getTimestamp(i) == null) ? null : rs.getTimestamp(i).toLocalDateTime());
            }
        } catch (Exception ex) {
            LOGGER.error(ERROR_MESSAGE, ex);
        } finally {
            dataBaseConfig.closeConnection(con);
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }
        return ticket;
    }

    /**
     * Update ticket
     *
     * @param ticket to update
     * @return true : success | false : failed
     */
    public boolean updateTicket(final Ticket ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        int i = 1;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(i, ticket.getPrice());
            i++;
            ps.setTimestamp(i, Timestamp.valueOf(ticket.getOutTime()));
            i++;
            ps.setInt(i, ticket.getId());
            ps.execute();
        } catch (Exception ex) {
            LOGGER.error("Error saving ticket info", ex);
            return false;
        } finally {
            dataBaseConfig.closeConnection(con);
            dataBaseConfig.closePreparedStatement(ps);
        }
        return true;
    }

    /**
     * Vérifie les anciens tickets
     *
     * @param vehicleRegNumber numéro d'immatriculation
     * @return countOldTickets
     */
    public int checkOldTicket(final String vehicleRegNumber) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int countOldTickets = 0;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.OLD_TICKETS);
            ps.setString(1, vehicleRegNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                countOldTickets = rs.getInt(1);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            LOGGER.error("Error checking old tickets", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }
        return countOldTickets;
    }
}
