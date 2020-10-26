package com.parkit.parkingsystem.model;

import java.time.LocalDateTime;

/**
 * Store ticket data.
 */
public class Ticket {

    private ParkingSpot parkingSpot;
    private LocalDateTime inTimeTicket;
    private LocalDateTime outTimeTicket;
    private String vehicleRegNumber;
    private int id;
    private double price;
    private static final double ROUNDER = 100.0;

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param identifiant the identifiant
     */
    public void setId(final int identifiant) {
        this.id = identifiant;
    }

    /**
     * Gets parking spot.
     *
     * @return the parking spot
     */
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    /**
     * Sets parking spot.
     *
     * @param parkSpot the park spot
     */
    public void setParkingSpot(final ParkingSpot parkSpot) {
        this.parkingSpot = parkSpot;
    }

    /**
     * Gets vehicle reg number.
     *
     * @return the vehicle reg number
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    /**
     * Sets vehicle reg number.
     *
     * @param vhlRegistration the vhl registration
     */
    public void setVehicleRegNumber(final String vhlRegistration) {
        this.vehicleRegNumber = vhlRegistration;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public double getPrice() {
        return Math.round(price * ROUNDER) / ROUNDER;
    }

    /**
     * Sets price.
     *
     * @param ticketPrice the ticket price
     */
    public void setPrice(final double ticketPrice) {
        this.price = ticketPrice;
    }

    /**
     * Gets in time.
     *
     * @return the in time
     */
    public LocalDateTime getInTime() {
        return inTimeTicket;
    }

    /**
     * Sets in time.
     *
     * @param ticketInTime the ticket in time
     */
    public void setInTime(final LocalDateTime ticketInTime) {
        this.inTimeTicket = ticketInTime;
    }

    /**
     * Gets out time.
     *
     * @return the out time
     */
    public LocalDateTime getOutTime() {
        return outTimeTicket;
    }

    /**
     * Sets out time.
     *
     * @param ticketOutTime the ticket out time
     */
    public void setOutTime(final LocalDateTime ticketOutTime) {
        this.outTimeTicket = ticketOutTime;
    }
}
