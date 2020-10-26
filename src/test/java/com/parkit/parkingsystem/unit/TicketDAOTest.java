package com.parkit.parkingsystem.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.database.DataBasePrepareService;
import com.parkit.parkingsystem.integration.database.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


public class TicketDAOTest {

    private static DataBaseTestConfig dataBaseConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService = new DataBasePrepareService();
    private static TicketDAO ticketDAO;
    private static Ticket ticket;
    private static String vehicleRegNumber = "ABCDEF";

    @BeforeAll
    private static void setUp() {
	ticketDAO = new TicketDAO();
    }

    @BeforeEach
    private void setUpPerTest() {
	dataBasePrepareService.clearDataBaseEntries();
	ticketDAO.setDataBaseConfig(dataBaseConfig);
	ticket = new Ticket();
	ticket.getId();
	ticket.setInTime(LocalDateTime.now().minusHours(24).minusMinutes(1));
	ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));
	ticket.setVehicleRegNumber(vehicleRegNumber);
	ticket.setPrice(Fare.CAR_RATE_PER_HOUR);
    }

	@Test
	@DisplayName("New user : vérifie nombre de visites")
	public void newUserCheckNumberOfVisites() {
		ticketDAO.saveTicket(ticket);
		Ticket ticketNewUser = ticketDAO.getTicket(vehicleRegNumber);
		ticketNewUser.setOutTime(LocalDateTime.now());
		ticketDAO.saveTicket(ticketNewUser);
		int numberUserVisits = ticketDAO.checkOldTicket("ZORGLUB");
		assertThat(numberUserVisits).isEqualTo(0);
		assertThat(ticketNewUser.getId()).isNotNull();
		assertThat(ticket.getId()).isNotNull();
	}

    @Test
    @DisplayName("Regular user : vérifie nombre de visites")
    public void regularUserCheckNumberOfVisites() {
	ticketDAO.saveTicket(ticket);
	Ticket ticketRegularUser = ticketDAO.getTicket(vehicleRegNumber);
	ticketRegularUser.setOutTime(LocalDateTime.now());
	ticketDAO.saveTicket(ticketRegularUser);
	int numberVisitsUser = ticketDAO.checkOldTicket(vehicleRegNumber);
	assertThat(numberVisitsUser).isEqualTo(1);
	assertThat(ticketRegularUser.getId()).isNotNull();
	assertThat(ticket.getId()).isNotNull();
    }

    @Test
    @Tag("updateTicket")
    @DisplayName("Update ticket sans out-time retourne false")
    public void updateTicketWithoutOutTime() {
	ticketDAO.saveTicket(ticket);
	ticket.setOutTime(null);
	boolean isUpdated = ticketDAO.updateTicket(ticket);
	assertThat(isUpdated).isFalse();
	assertThat(ticket.getId()).isNotNull();
    }

	@Test
	@DisplayName("Update ticket avec out-time retourne true")
	public void updateTicketWithOutTime() {
		ticketDAO.saveTicket(ticket);
		ticket.setOutTime(LocalDateTime.now());
		boolean isUpdated = ticketDAO.updateTicket(ticket);
		assertThat(isUpdated).isTrue();
		assertThat(ticket.getId()).isNotNull();
	}



}