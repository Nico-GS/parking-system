package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.database.DataBasePrepareService;
import com.parkit.parkingsystem.integration.database.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;


/**
 * The type Parking data base it.
 */
@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

   private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
   private static ParkingSpotDAO parkingSpotDAO;
   private static TicketDAO ticketDAO;
   private static DataBasePrepareService dataBasePrepareService;
   private static final String vehicleRegNumber = "ABCDEF";

   @Mock
   private static InputReaderUtil inputReaderUtil;
   @Mock
   private static ParkingSpot parkingSpot;

   /**
    * Marque une pause d'une demi seconde entre l'entrée et la sortie
    * @throws InterruptedException interruption exception
    */
   private synchronized void demiSecond() throws InterruptedException {
      final int HALF_SECOND = 500;
      Thread.sleep(HALF_SECOND);
   }

   @BeforeAll
   private static void setUp() throws Exception {
      parkingSpotDAO = new ParkingSpotDAO();
      ticketDAO = new TicketDAO();
      dataBasePrepareService = new DataBasePrepareService();
      parkingSpotDAO.setDataBaseConfig(dataBaseTestConfig);
   }

   @BeforeEach
   private void setUpPerTest() throws Exception {
      dataBasePrepareService.clearDataBaseEntries();
      when(inputReaderUtil.readSelection()).thenReturn(1);
      when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
      ticketDAO.setDataBaseConfig(dataBaseTestConfig);
   }

   @Test
   @DisplayName("Vérifie qu'un ticket est bien save en DB et que la table est mise à jour")
   public void checkIfTicketIsSavedInDataBaseAndUpdateTable() {
      ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
      parkingService.processIncomingVehicle();
      parkingSpot = parkingService.getNextParkingNumberIfAvailable();
      assertThat(ticketDAO.getTicket(vehicleRegNumber)).isNotNull(); // Vérifie ticket save in DB
      assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR) > 1).isTrue(); // Vérifie disponibilité place
   }

   @Test
   @DisplayName("Vérifie user recurrent et premier user, vérifie le nombre de visite, et les bons numéros d'enregistrement")
   public void recurrentAndNewUserAndCheckNumberOfVisits() throws InterruptedException {
      ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
      parkingService.processIncomingVehicle();
      demiSecond();
      parkingService.processExitingVehicle();
      int visitRecurrentUser = ticketDAO.checkOldTicket(vehicleRegNumber);
      int visitNewUser = ticketDAO.checkOldTicket("012345");
      assertThat(1).isEqualTo(visitRecurrentUser);
      assertThat(0).isEqualTo(visitNewUser);
   }

   @Test
   @DisplayName("Vérifie que le tarif et l'heure de départ sont correctement renseigné dans la DB")
   public void checkFareAndOutTimeInDb() throws InterruptedException {
      ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
      parkingService.processIncomingVehicle();
      demiSecond();
      parkingService.processExitingVehicle();
      assertThat(ticketDAO.getTicket(vehicleRegNumber).getOutTime()).isNotNull();
      assertThat(ticketDAO.getTicket(vehicleRegNumber).getPrice()).isNotNull();
      assertThat(ticketDAO.getTicket(vehicleRegNumber).getPrice()).isEqualTo(0.0);
   }
}
