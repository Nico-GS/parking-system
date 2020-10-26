package com.parkit.parkingsystem.unit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class FareCalculatorServiceTest {

   private static FareCalculatorService fareCalculatorService;
   private Ticket ticket;
   private ParkingSpot parkingSpot;

   @BeforeAll
   private static void setUp() {
      fareCalculatorService = new FareCalculatorService();
   }

   @BeforeEach
   private void setUpPerTest() {
      ticket = new Ticket();
      parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
      ticket.setInTime(LocalDateTime.now());
      ticket.setOutTime(null);
      ticket.setParkingSpot(parkingSpot);
   }

   // PARTIE CAR

   @Test
   @DisplayName("New user : calcul le prix d'une heure")
   public void calculatePriceForOneHour() {
      ticket.setOutTime(ticket.getInTime().plusHours(1));
      fareCalculatorService.calculateFare(ticket, false);
      assertThat(Fare.CAR_RATE_PER_HOUR).isEqualTo(ticket.getPrice());
   }

   @Test
   @DisplayName("Regular user : calcul le prix d'une heure")
   public void calculatePriceForOneHourForRecurrentUser() {
      ticket.setOutTime(ticket.getInTime().plusHours(1));
      fareCalculatorService.calculateFare(ticket, true);
      assertThat(Math.round((0.95 * Fare.CAR_RATE_PER_HOUR) * 100.0) / 100.0).isEqualTo(ticket.getPrice());
   }

   @Test
   @DisplayName("New user : tarif pour 45 minutes durée entre 30 et 45 minutes, retourne tarif des 15 minutes")
   public void parkingTimeBetweenThirtyAndFortyFiveMinutesNewUser() {
      ticket.setOutTime(ticket.getInTime().plusMinutes(45));
      fareCalculatorService.calculateFare(ticket, false);
      assertThat(ticket.getPrice()).isEqualTo(Math.round((0.75 * Fare.CAR_RATE_PER_HOUR) * 100.0) / 100.0);
   }

   @Test
   @DisplayName("Regular user : tarif pour 45 minutes durée entre 30 et 45 minutes, retourne tarif des 15 minutes")
   public void parkingTimeBetweenThirtyAndFortyFiveMinutesRegularUser() {
      ticket.setOutTime(ticket.getInTime().plusMinutes(45));
      fareCalculatorService.calculateFare(ticket, true);
      assertThat(ticket.getPrice()).isEqualTo((Math.round((0.75 * 0.95 * Fare.CAR_RATE_PER_HOUR) * 100.0) / 100.0));
   }

   @Test
   @DisplayName("New user - Stationnement de 30 minutes tarif doit etre egal à 0.75%")
   public void thirtyMinutesParkingTimeFareEqualToZeroDotSoixanteQuinzeNewUser() {
      ticket.setOutTime(ticket.getInTime().plusMinutes(30));
      fareCalculatorService.calculateFare(ticket, false);
      assertThat(ticket.getPrice()).isEqualTo((Math.round((0.75 * Fare.CAR_RATE_PER_HOUR) * 100.0) / 100.0));
   }

   @Test
   @DisplayName("User régulier - Stationnement de 30 minutes tarif doit etre egal à 0.75%")
   public void thirtyMinutesParkingTimeFareEqualToZeroDotSoixanteQuinzeRecurrentUser() {
      ticket.setOutTime(ticket.getInTime().plusMinutes(30));
      fareCalculatorService.calculateFare(ticket, true);
      assertThat(ticket.getPrice()).isEqualTo((Math.round((0.75 * 0.95 * Fare.CAR_RATE_PER_HOUR) * 100.0) / 100.0));
   }

   @Test
   @DisplayName("Nouvel user - 30 minutes gratuites")
   public void thirtyMinutesFreeNewUser() {
      ticket.setOutTime(ticket.getInTime().plusMinutes(29).plusSeconds(59));
      fareCalculatorService.calculateFare(ticket, false);
      assertThat(ticket.getPrice()).isEqualTo(0);
   }

   @Test
   @DisplayName("User régulier - 30 minutes gratuites")
   public void thirtyMinutesFreeForRecurrentUser() {
      ticket.setOutTime(ticket.getInTime().plusMinutes(29).plusSeconds(59));
      fareCalculatorService.calculateFare(ticket, true);
      assertThat(ticket.getPrice()).isEqualTo(0);
   }

   @Test
   @DisplayName("New user - 24h de stationnement = tarif de 24h")
   public void newUserTimeParkingTwentyFourHours() {
      ticket.setOutTime(ticket.getInTime().plusDays(1));
      fareCalculatorService.calculateFare(ticket, false);
      assertThat(ticket.getPrice()).isEqualTo(Math.round((24 * Fare.CAR_RATE_PER_HOUR) * 100.0) / 100.0);
   }

   @Test
   @Tag("CAR")
   @DisplayName("Regular user - 24h de stationnement = tarif de 24h")
   public void regularUserTimeParkingTwentyFourHours() {
      ticket.setOutTime(ticket.getInTime().plusDays(1));
      fareCalculatorService.calculateFare(ticket, true);
      assertThat(ticket.getPrice()).isEqualTo(Math.round(24 * 0.95 * Fare.CAR_RATE_PER_HOUR * 100.0) / 100.0);
   }

   @Test
   @DisplayName("New user : Exception")
   public void newUserException() {
      parkingSpot.setParkingType(ParkingType.CAR);
      assertThatNullPointerException().isThrownBy(() -> {
         fareCalculatorService.calculateFare(ticket, false);
      });
   }

   @Test
   @DisplayName("Regular user : Exception")
   public void regularUserException() {
      parkingSpot.setParkingType(ParkingType.CAR);
      assertThatNullPointerException().isThrownBy(() -> {
         fareCalculatorService.calculateFare(ticket, true);
      });
   }

   @Test
   @DisplayName("New user : Exception null out-time")
   public void newUserExceptionNullOutTime() {
      parkingSpot.setParkingType(ParkingType.CAR);
      ticket.setOutTime(null);
      assertThatNullPointerException().isThrownBy(() -> {
         fareCalculatorService.calculateFare(ticket, false);
      });
   }

   @Test
   @DisplayName("Regular user : Exception null out-time")
   public void RegularUserExceptionNullOutTime() {
      parkingSpot.setParkingType(ParkingType.CAR);
      ticket.setOutTime(null);
      assertThatNullPointerException().isThrownBy(() -> {
         fareCalculatorService.calculateFare(ticket, true);
      });
   }

   @Test
   @DisplayName("New user : Exception out-time < in-time")
   public void newUserExceptionOutTimeAndInTime() {
      parkingSpot.setParkingType(ParkingType.CAR);
      ticket.setOutTime(ticket.getInTime().minusHours(1));
      assertThatNullPointerException().isThrownBy(() -> {
         fareCalculatorService.calculateFare(ticket, false);
      });
   }

   @Test
   @DisplayName("Regular user : Exception out-time < in-time")
   public void regularUserExceptionOutTimeAndInTime() {
      parkingSpot.setParkingType(ParkingType.CAR);
      ticket.setOutTime(ticket.getInTime().minusHours(1));
      assertThatNullPointerException().isThrownBy(() -> {
         fareCalculatorService.calculateFare(ticket, true);
      });
   }

   // PARTIE BIKE

   @Test
   @DisplayName("New user : calcul le prix d'une heure")
   public void newUserCalculFareOneHour() {
      ticket.setOutTime(ticket.getInTime().plusHours(1));
      parkingSpot.setParkingType(ParkingType.BIKE);
      fareCalculatorService.calculateFare(ticket, false);
      assertThat(Fare.BIKE_RATE_PER_HOUR).isEqualTo(ticket.getPrice());
   }

   @Test
   @DisplayName("Regular user : calcul le prix d'une heure")
   public void regularUserCalculFareOneHour() {
      ticket.setOutTime(ticket.getInTime().plusHours(1));
      parkingSpot.setParkingType(ParkingType.BIKE);
      fareCalculatorService.calculateFare(ticket, true);
      assertThat(0.95 * Fare.BIKE_RATE_PER_HOUR).isEqualTo(ticket.getPrice());
   }

   @Test
   @DisplayName("New user : tarif pour 45 minutes durée entre 30 et 45 minutes, retourne tarif des 15 minutes")
   public void newUserBikeFareForThirtyMinutes() {
      ticket.setOutTime(ticket.getInTime().plusMinutes(45));
      parkingSpot.setParkingType(ParkingType.BIKE);
      fareCalculatorService.calculateFare(ticket, false);
      assertThat(ticket.getPrice()).isEqualTo(0.75 * Fare.BIKE_RATE_PER_HOUR);
   }

   @Test
   @DisplayName("regular user - 45 minutes parking ")
   public void regularUserBikeFareForThirtyMinutes() {
      ticket.setOutTime(ticket.getInTime().plusMinutes(45));
      parkingSpot.setParkingType(ParkingType.BIKE);
      fareCalculatorService.calculateFare(ticket, true);
      assertThat(ticket.getPrice()).isEqualTo((Math.round((0.75 * 0.95 * Fare.BIKE_RATE_PER_HOUR) * 100.0) / 100.0));
   }

   @Test
   @DisplayName("New user - Stationnement de 30 minutes tarif doit etre egal à 0.75%")
   public void thirtyMinutesParkingTimeFareEqualToZeroDotSoixanteQuinzeNewUserBike() {
      ticket.setOutTime(ticket.getInTime().plusMinutes(30));
      parkingSpot.setParkingType(ParkingType.BIKE);
      fareCalculatorService.calculateFare(ticket, false);
      assertThat(ticket.getPrice()).isEqualTo((Math.round((0.75 * Fare.BIKE_RATE_PER_HOUR) * 100.0) / 100.0));
   }

   @Test
   @DisplayName("Regular user - Stationnement de 30 minutes tarif doit etre egal à 0.75%")
   public void thirtyMinutesParkingTimeFareEqualToZeroDotSoixanteQuinzeRegularUserBike() {
      ticket.setOutTime(ticket.getInTime().plusMinutes(30));
      parkingSpot.setParkingType(ParkingType.BIKE);
      fareCalculatorService.calculateFare(ticket, true);
      assertThat(ticket.getPrice()).isEqualTo((Math.round((0.75 * 0.95 * Fare.BIKE_RATE_PER_HOUR) * 100.0) / 100.0));
   }

   @Test
   @DisplayName("New user - 30 minutes gratuites")
   public void thirtyMinutesFreeNewUserBike() {
      ticket.setOutTime(ticket.getInTime().plusMinutes(29).plusSeconds(59));
      parkingSpot.setParkingType(ParkingType.BIKE);
      fareCalculatorService.calculateFare(ticket, false);
      assertThat(ticket.getPrice()).isEqualTo(0);
   }

   @Test
   @DisplayName("Regular user - 30 minutes gratuites")
   public void thirtyMinutesFreeRegularUserBike() {
      ticket.setOutTime(ticket.getInTime().plusMinutes(29).plusSeconds(59));
      parkingSpot.setParkingType(ParkingType.BIKE);
      fareCalculatorService.calculateFare(ticket, true);
      assertThat(ticket.getPrice()).isEqualTo(0);
   }

   @Test
   @DisplayName("New user : exception")
   public void newUserExceptionBike() {
      parkingSpot.setParkingType(ParkingType.BIKE);
      assertThatNullPointerException().isThrownBy(() -> {
         fareCalculatorService.calculateFare(ticket, false);
      });
   }

   @Test
   @DisplayName("Regular user : exception")
   public void regularUserExceptionBike() {
      parkingSpot.setParkingType(ParkingType.BIKE);
      assertThatNullPointerException().isThrownBy(() -> {
         fareCalculatorService.calculateFare(ticket, true);
      });
   }

   @Test
   @DisplayName("UnknowType - Impossible calculation")
   public void givenUnknow() {
      ticket.setOutTime(ticket.getInTime().plusHours(1));
      parkingSpot.setParkingType(null);
      assertThatNullPointerException().isThrownBy(() -> {
         fareCalculatorService.calculateFare(ticket, false);
      });
   }

   @Test
   @DisplayName("New user : Exception null out-time")
   public void newUserExceptionNullOutTimeBike() {
      parkingSpot.setParkingType(ParkingType.BIKE);
      ticket.setOutTime(null);
      assertThatNullPointerException().isThrownBy(() -> {
         fareCalculatorService.calculateFare(ticket, false);
      });
   }

   @Test
   @DisplayName("Regular user : Exception null out-time")
   public void RegularUserExceptionNullOutTimeBike() {
      parkingSpot.setParkingType(ParkingType.BIKE);
      ticket.setOutTime(null);
      assertThatNullPointerException().isThrownBy(() -> {
         fareCalculatorService.calculateFare(ticket, true);
      });
   }
}
