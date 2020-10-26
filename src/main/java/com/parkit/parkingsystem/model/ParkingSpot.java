package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

public class ParkingSpot {

    private int number;
    private ParkingType parkingType;
    private boolean isAvailable;

    public ParkingSpot(final int nb, final ParkingType parkType, final boolean available) {
        this.number = nb;
        this.parkingType = parkType;
        this.isAvailable = available;
    }

    public int getId() {
        return number;
    }

    public void setId(final int nb) {
        this.number = nb;
    }

    public ParkingType getParkingType() {
        return parkingType;
    }

    public void setParkingType(final ParkingType parkType) {
        this.parkingType = parkType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

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
