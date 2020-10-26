package com.parkit.parkingsystem.unit;


import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;
    private static Ticket ticket;
    private static ParkingSpot parkingSpot;
    private static LocalDateTime inTime;
    private static String vehicleRegNumber = "ABCDEF";

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @Mock
    private static FareCalculatorService fareCalculatorService;

    @BeforeEach
    private void setUpPerTest() {
        inTime = LocalDateTime.now().minusHours(24).minusMinutes(1);
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket = new Ticket();
            ticket.setInTime(inTime);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber(vehicleRegNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }


    @Test
    @DisplayName("New user : car entrant")
    public void newUserCheckNewIncomingCar() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        parkingService.processIncomingVehicle();
        verify(inputReaderUtil, times(1)).readSelection();
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any(ParkingType.class));
        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, times(1)).getTicket(anyString());
    }

    @Test
    @DisplayName("Regular user : car entrant")
    public void regularUserCheckNewIncomingCar() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        when(ticketDAO.checkOldTicket(anyString())).thenReturn(1);
        parkingService.processIncomingVehicle();
        verify(ticketDAO, times(1)).checkOldTicket(anyString());
    }

    @Test
    @DisplayName("New user : bike entrant")
    public void newUserCheckNewIncomingBike() {
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        parkingService.processIncomingVehicle();
        verify(inputReaderUtil, times(1)).readSelection();
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any(ParkingType.class));
        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, times(1)).getTicket(anyString());
    }

    @Test
    @DisplayName("Regular user : bike entrant")
    public void regularUserCheckNewIncomingBike() {
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        when(ticketDAO.checkOldTicket(anyString())).thenReturn(1);
        parkingService.processIncomingVehicle();
        verify(ticketDAO, times(1)).checkOldTicket(anyString());
    }

    @Test
    @DisplayName("New user : bike sortant")
    public void newUserCheckNewExitingBike() {
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        parkingService.processExitingVehicle();
        verify(ticketDAO, times(1)).checkOldTicket(anyString());
        verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    @DisplayName("Regular user : bike sortant")
    public void regularUserCheckNewExitingBike() {
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(ticketDAO.checkOldTicket(anyString())).thenReturn(2);
        parkingService.processExitingVehicle();
        verify(ticketDAO, times(1)).checkOldTicket(anyString());
    }

    @Test
    @DisplayName("New user : Véhicule sortant, prix du ticket")
    public void newUserVehicleExitingTicketFare() {
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(ticketDAO.checkOldTicket(anyString())).thenReturn(0);
        int numberVisitsUser = ticketDAO.checkOldTicket(ticket.getVehicleRegNumber());
        boolean isRegularUser = numberVisitsUser >= 1;
        fareCalculatorService.calculateFare(ticket, isRegularUser);
        parkingService.processExitingVehicle();
        verify(fareCalculatorService).calculateFare(ticket, false);
    }

    @Test
    @DisplayName("Regular user Véhicule sortant, prix du ticket avec discount")
    public void regularUserVehicleExitingWithDiscountFare() {
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(ticketDAO.checkOldTicket(anyString())).thenReturn(2);
        int numberUserVisit = ticketDAO.checkOldTicket(ticket.getVehicleRegNumber());
        boolean isRegularUser = numberUserVisit >= 1;
        fareCalculatorService.calculateFare(ticket, isRegularUser);
        parkingService.processExitingVehicle();
        verify(fareCalculatorService).calculateFare(ticket, true);
    }
}