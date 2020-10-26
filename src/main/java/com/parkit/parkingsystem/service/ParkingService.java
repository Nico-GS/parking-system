package com.parkit.parkingsystem.service;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;


/**
 * The type Parking service.
 */
public class ParkingService {

    private static final FareCalculatorService fareCalculatorService = new FareCalculatorService();
    private final InputReaderUtil inputReaderUtil;
    private final ParkingSpotDAO parkingSpotDAO;
    private final TicketDAO ticketDAO;
    private static final Logger LOGGER = LogManager.getLogger("ParkingService");
    private static final String ERROR_PARKED_VHL = "Registration already occupied. Enter a valid registration";
    private static final String ERROR_EXITED_VHL = "Registration already exited. Enter a valid registration";
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final int RECURRENT_USER = 1;
    private static final int DISCOUNT = 5;

    /**
     * Instantiates a new Parking service.
     *
     * @param inputReader    the input reader
     * @param daoParkingSpot the dao parking spot
     * @param daoTicket      the dao ticket
     */
    public ParkingService(final InputReaderUtil inputReader,
                          final ParkingSpotDAO daoParkingSpot, final TicketDAO daoTicket) {
        this.inputReaderUtil = inputReader;
        this.parkingSpotDAO = daoParkingSpot;
        this.ticketDAO = daoTicket;
    }

    /**
     * Incoming vehicle management
     */
    public void processIncomingVehicle() {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();

            if (parkingSpot != null && parkingSpot.getId() > 0) { // Vérifie si un véhicule à déjà un ticket
                String vehicleRegNumber = getVehicleRegNumber();
                Ticket parkedVehicle = ticketDAO.getTicket(vehicleRegNumber);

                while (parkedVehicle != null && parkedVehicle.getOutTime() == null) { // Quand numéro enregistrement déjà entré
                    LOGGER.error(ERROR_PARKED_VHL);
                    vehicleRegNumber = getVehicleRegNumber();
                    parkedVehicle = ticketDAO.getTicket(vehicleRegNumber);
                }
                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot);
                LocalDateTime inTime = LocalDateTime.now();
                String inTimeFormatter = inTime.format(format);
                Ticket ticket = new Ticket();
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0);
                ticket.setInTime(inTime);
                ticket.setOutTime(null);
                ticketDAO.saveTicket(ticket);
                int numberVisitsUser = ticketDAO.checkOldTicket(ticket.getVehicleRegNumber());
                // user recurrent
                if (numberVisitsUser >= RECURRENT_USER) { //
                    LOGGER.info("Regular user, reduction of {}% ", DISCOUNT);
                }
                LOGGER.info("Generated Ticket and saved in DB");
                LOGGER.info("Please park your vehicle in spot number: {}",
                        parkingSpot.getId());
                LOGGER.info("Recorded in-time for vehicle number: {} is: {}",
                        vehicleRegNumber, inTimeFormatter);
            } else {
                LOGGER.error("Parking full");
            }
        } catch (Exception e) {
            LOGGER.error("Unable to process incoming vehicle");
        }
    }

    /**
     * Get the vehicle registration number
     *
     * @return the registration number
     */
    private String getVehicleRegNumber() {
        LOGGER.info("Please type the vehicle registration number "
                + "and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }


    /**
     * Gets parking spot
     *
     * @return the next parking number if available
     */
    public ParkingSpot getNextParkingNumberIfAvailable() {
        int parkingNumber = 0;
        ParkingSpot parkingSpot = null;
        try {
            ParkingType parkingType = getVehicleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if (parkingNumber > 0) {
                parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
            } else {
                throw new SQLException("Error fetching parking number from DB."
                        + " Parking slots might be full");
            }
        } catch (IllegalArgumentException ie) {
            LOGGER.error("Error parsing user input for type of vehicle", ie);
        } catch (Exception e) {
            LOGGER.error("Error fetching next available parking slot", e);
        }
        return parkingSpot;
    }

    /**
     * Get the vehicle type
     * 1 : Car
     * 2 : Bike
     */
    private ParkingType getVehicleType() {
        LOGGER.info("Please select vehicle type from menu");
        LOGGER.info("1 CAR");
        LOGGER.info("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch (input) {
            case 1:
                return ParkingType.CAR;
            case 2:
                return ParkingType.BIKE;
            default:
                LOGGER.error("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
        }
    }


    /**
     * Process exiting vehicle
     */
    public void processExitingVehicle() {
        try {
            String vehicleRegNumber = getVehicleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            while (ticket.getOutTime() != null) { // Ne pas récupérer un ticket déjà payé et qui a quitté le parking
                LOGGER.error(ERROR_EXITED_VHL);
                vehicleRegNumber = getVehicleRegNumber();
                ticket = ticketDAO.getTicket(vehicleRegNumber);
            }

            LocalDateTime inTime = ticket.getInTime();
            String inTimeFormatter = inTime.format(format);
            LocalDateTime outTime = LocalDateTime.now();
            String outTimeFormatter = outTime.format(format);
            ticket.setOutTime(outTime);
            int numberVisitsUser = ticketDAO.checkOldTicket(ticket.getVehicleRegNumber());
            fareCalculatorService.calculateFare(ticket, numberVisitsUser >= RECURRENT_USER);

            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                DecimalFormat roundedPrice = new DecimalFormat("#0.00 €");
                String finalRoundedPrice = roundedPrice.format(ticket.getPrice());
                LOGGER.info("Recorded in-time : {}", inTimeFormatter);
                LOGGER.info("Please pay the parking fare: {}", finalRoundedPrice);
                LOGGER.info("Recorded out-time for vehicle number: {} is: {}", ticket.getVehicleRegNumber(), outTimeFormatter);
            } else {
                LOGGER.error("Unable to update ticket information. Error occurred.");
            }
        } catch (Exception e) {
            LOGGER.error("Unable to process exiting vehicle."
                    + " Please verify the entry vehicle registration number.");
        }
    }
}
