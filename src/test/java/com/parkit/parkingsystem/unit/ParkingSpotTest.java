package com.parkit.parkingsystem.unit;


import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * Parking spot test
 */
public class ParkingSpotTest {

    private ParkingSpot parkingSpot;

    @Test
    @DisplayName("Car : place numéro 1 disponible")
    public void carSlotOneAvailable() {
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        assertThat(parkingSpot.getId()).isEqualTo(1);
        assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.CAR);
        assertThat(parkingSpot.isAvailable()).isTrue();
    }

    @Test
    @DisplayName("Car : place numéro 2 disponible")
    public void carSlotTwoAvailable() {
        parkingSpot = new ParkingSpot(2, ParkingType.CAR, true);
        assertThat(parkingSpot.getId()).isEqualTo(2);
        assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.CAR);
        assertThat(parkingSpot.isAvailable()).isTrue();
    }

    @Test
    @DisplayName("Car : place numéro 3 disponible")
    public void carSlotThreeAvailable() {
        parkingSpot = new ParkingSpot(3, ParkingType.CAR, true);
        assertThat(parkingSpot.getId()).isEqualTo(3);
        assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.CAR);
        assertThat(parkingSpot.isAvailable()).isTrue();
    }

    @Test
    @DisplayName("Car : place numéro 4 disponible")
    public void carSlotFourAvailable() {
        parkingSpot = new ParkingSpot(4, ParkingType.CAR, true);
        assertThat(parkingSpot.getId()).isEqualTo(4);
        assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.CAR);
        assertThat(parkingSpot.isAvailable()).isTrue();
    }

    @Test
    @DisplayName("Bike - place numéro 2 indisponible")
    public void bikeSlotTwoUnavailable() {
        parkingSpot = new ParkingSpot(2, ParkingType.BIKE, false);
        assertThat(parkingSpot.getId()).isEqualTo(2);
        assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.BIKE);
        assertThat(parkingSpot.isAvailable()).isFalse();
    }

    @Test
    @DisplayName("Bike - place numéro 3 indisponible")
    public void bikeSlotThreeUnavailable() {
        parkingSpot = new ParkingSpot(3, ParkingType.BIKE, false);
        assertThat(parkingSpot.getId()).isEqualTo(3);
        assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.BIKE);
        assertThat(parkingSpot.isAvailable()).isFalse();
    }

    @Test
    @DisplayName("Bike - place numéro 4 indisponible")
    public void bikeSlotFourUnavailable() {
        parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
        assertThat(parkingSpot.getId()).isEqualTo(4);
        assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.BIKE);
        assertThat(parkingSpot.isAvailable()).isFalse();
    }

    @Test
    @DisplayName("Null parking")
    public void nullParking() {
        parkingSpot = new ParkingSpot(1, null, false);
        assertThat(parkingSpot.getParkingType()).isEqualTo(null);
        assertThat(parkingSpot.isAvailable()).isFalse();
    }
}