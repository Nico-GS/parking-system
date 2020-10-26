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

    public int getId() {
        return id;
    }

    public void setId(final int identifiant) {
        this.id = identifiant;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(final ParkingSpot parkSpot) {
        this.parkingSpot = parkSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(final String vhlRegistration) {
        this.vehicleRegNumber = vhlRegistration;
    }

    public double getPrice() {
        return Math.round(price * ROUNDER) / ROUNDER;
    }

    public void setPrice(final double ticketPrice) {
        this.price = ticketPrice;
    }

    public LocalDateTime getInTime() {
        return inTimeTicket;
    }

    public void setInTime(final LocalDateTime ticketInTime) {
        this.inTimeTicket = ticketInTime;
    }

    public LocalDateTime getOutTime() {
        return outTimeTicket;
    }

    public void setOutTime(final LocalDateTime ticketOutTime) {
        this.outTimeTicket = ticketOutTime;
    }
}
