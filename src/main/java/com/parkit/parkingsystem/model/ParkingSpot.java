package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * The type Parking spot.
 */
public class ParkingSpot {

    private int number;
    private ParkingType parkingType;
    private boolean isAvailable;

    /**
     * Instantiates a new Parking spot.
     *
     * @param nb        the nb
     * @param parkType  the park type
     * @param available the available
     */
    public ParkingSpot(final int nb, final ParkingType parkType, final boolean available) {
        this.number = nb;
        this.parkingType = parkType;
        this.isAvailable = available;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return number;
    }

    /**
     * Sets id.
     *
     * @param nb the nb
     */
    public void setId(final int nb) {
        this.number = nb;
    }

    /**
     * Gets parking type.
     *
     * @return the parking type
     */
    public ParkingType getParkingType() {
        return parkingType;
    }

    /**
     * Sets parking type.
     *
     * @param parkType the park type
     */
    public void setParkingType(final ParkingType parkType) {
        this.parkingType = parkType;
    }

    /**
     * Is available boolean.
     *
     * @return the boolean
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Sets available.
     *
     * @param available the available
     */
    public void setAvailable(final boolean available) {
        isAvailable = available;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return number;
    }
}
