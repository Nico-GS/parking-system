package com.parkit.parkingsystem.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.util.InputReaderUtil;


/**
 * The interactive shell
 */
public final class InteractiveShell {

    private static final Logger LOGGER = LogManager.getLogger("InteractiveShell");
    private static final int QUIT = 3;

    private InteractiveShell() {
    }

    /**
     * Load the user interface
     */
    public static void loadInterface() {

        LOGGER.info("Welcome to Parking System!");
        boolean continueApp = true;
        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
        TicketDAO ticketDAO = new TicketDAO();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        while (continueApp) {
            loadMenu();
            int option = inputReaderUtil.readSelection();
            switch (option) {
                case 1:
                    parkingService.processIncomingVehicle();
                    break;
                case 2:
                    parkingService.processExitingVehicle();
                    break;
                case QUIT:
                    LOGGER.info("Exiting from the system!");
                    continueApp = false;
                    break;
                default:
                    LOGGER.info("Unsupported option. Please enter a "
                            + "number corresponding to the provided menu");
            }
        }
    }

    /**
     * Load the user menu
     */
    private static void loadMenu() {
        LOGGER.info("5% discount for recurrent user and 30 minutes free for all");
        LOGGER.info("Please select an option."
                + " Simply enter the number to choose an action");
        LOGGER.info("1 New Vehicle Entering - Allocate Parking Space");
        LOGGER.info("2 Vehicle Exiting - Generate Ticket Price");
        LOGGER.info("3 Shutdown System");
    }
}
