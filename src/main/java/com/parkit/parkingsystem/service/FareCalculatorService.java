package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * Calcul the tickets price during exiting process
 */
public class FareCalculatorService {

    private static final Logger LOGGER = LogManager.getLogger("FareCalculatorService");
    private static final int THIRTY_MINUTES = 30;
    private static final int FORTY_FIVE_MINUTES = 45; // Seuil de 45 minutes
    private static final double FORTY_FIVE_MINUTES_FARE = 0.75; // Calculer tarif de 45 minutes
    private static final double PERCENT_DISCOUNT_RECURRENT = 0.95; // Pourcentage de réduction user recurrent
    private static final int DISCOUNT_PERCENT_INFO = 5; // Compter ticket user recurrent

    /**
     * Calcul prix du ticket pour différentes durées et utilisateurs récurrents
     */
    public void calculateFare(final Ticket ticket, final boolean isRegularUser) {
        errorOutTime(ticket);
        double vehicleRatePerHour = 0;
        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR:
                vehicleRatePerHour = Fare.CAR_RATE_PER_HOUR;
                break;
            case BIKE:
                vehicleRatePerHour = Fare.BIKE_RATE_PER_HOUR;
                break;
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }

        LocalDateTime inTime = ticket.getInTime();
        LocalDateTime outTime = ticket.getOutTime();
        Duration durationOfTicket = Duration.between(inTime, outTime);
        LocalDateTime thirtyMinutes = inTime.plusMinutes(THIRTY_MINUTES); // 30 minutes gratuites
        LocalDateTime fortyFive = inTime.plusMinutes(FORTY_FIVE_MINUTES); // 45 minutes
        LocalDateTime hourThreshold = inTime.plusHours(1); // Delais après reception du ticket
        double ticketPrice = 0;
        if (outTime.isBefore(thirtyMinutes)) { // Moins de 30 minutes
            ticketPrice = 0;
        } else if ((outTime.isBefore(fortyFive) || outTime.isEqual(fortyFive)) && (outTime.isAfter(thirtyMinutes) // Entre 30 minutes et 45 minutes
                || outTime.isEqual(thirtyMinutes))) {
            ticketPrice = (FORTY_FIVE_MINUTES_FARE * vehicleRatePerHour);
        } else if ((outTime.isBefore(hourThreshold) // Entre 45 minutes et 1 heure
                || outTime.isEqual(hourThreshold))
                && outTime.isAfter(fortyFive)) {
            ticketPrice = vehicleRatePerHour;
        } else if (outTime.isAfter(hourThreshold)) { // Stationnement plus d'une heure
            ticketPrice = durationOfTicket.toHours() * vehicleRatePerHour;
        } else {
            throw new IllegalArgumentException("An error was occured. "
                    + "Please try again in a few moments "
                    + "or contact our technical support.");
        }
        ticket.setPrice(ticketPrice);
        // 5% discount pour user recurrent
        if (isRegularUser) {
            ticket.setPrice(PERCENT_DISCOUNT_RECURRENT * ticket.getPrice());
            LOGGER.info("Recurrent user : {}% discount", DISCOUNT_PERCENT_INFO);
        }
    }

    /**
     * Check errors during the exit parking process.
     */
    private static void errorOutTime(final Ticket ticket) {
        if ((ticket.getOutTime() == null)) {
            throw new NullPointerException("Error out time" + " NullPointerException");
        }
        if (ticket.getOutTime().compareTo(ticket.getInTime()) <= 0) {
            throw new NullPointerException("Error out time" + ticket.getOutTime().toString());
        }
    }
}
