package com.parkit.parkingsystem.unit;

import static org.assertj.core.api.Assertions.assertThat;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * Test DAO Parking Spot
 */
public class ParkingSpotDAOTest {

    private static ParkingSpotDAO parkingSpotDAO;
    private static ParkingSpot parkingSpot, parkingSpot2, parkingSpot3;

    @BeforeEach
    public void setUpPerTest() {
        parkingSpotDAO = new ParkingSpotDAO();
    }

    // CAR
    @Test
    @DisplayName("Car : places de parking disponibles retourne 1")
    public void carParkingSlotAvailableReturnOne() {
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        parkingSpotDAO.updateParking(parkingSpot);
        int slotAvailable = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertThat(slotAvailable).isEqualTo(1);
    }

    @Test
    @DisplayName("Car : place 1 et 2 indisponibles retourne 3")
    public void carParkingSlotUnavailableReturnThree() {
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        parkingSpotDAO.updateParking(parkingSpot);
        parkingSpot2 = new ParkingSpot(2, ParkingType.CAR, false);
        parkingSpotDAO.updateParking(parkingSpot2);
        parkingSpot3 = new ParkingSpot(3, ParkingType.CAR, true);
        parkingSpotDAO.updateParking(parkingSpot3);
        int availableSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertThat(availableSlot).isEqualTo(3);
    }

    @Test
    @DisplayName("Bike : places de parking disponibles retourne 4")
    public void bikeParkingSlotAvailableReturnFour() {
        parkingSpot = new ParkingSpot(1, ParkingType.BIKE, true);
        parkingSpotDAO.updateParking(parkingSpot);
        int availableSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        assertThat(availableSlot).isEqualTo(4);
    }

    @Test
    @DisplayName("Parking NULL")
    public void nullParkingSlot() {
        parkingSpot = new ParkingSpot(1, null, true);
        parkingSpotDAO.updateParking(parkingSpot);
        int availableSlot = parkingSpotDAO.getNextAvailableSlot(null);
        parkingSpotDAO.getNextAvailableSlot(null);
        assertThat(availableSlot).isEqualTo(-1);
    }
}