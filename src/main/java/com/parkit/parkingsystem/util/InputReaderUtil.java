package com.parkit.parkingsystem.util;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Read keyboard inputs
 */
public class InputReaderUtil {

   private Scanner scan = new Scanner((System.in), "UTF-8");
   private static final Logger LOGGER = LogManager.getLogger("InputReaderUtil");
   private static final String ERROR_MESSAGE = "Error while reading user input from Shell.";
   private static final int MINI_INPUT_SELECTION = 1;
   private static final int MAXI_INPUT_SELECTION = 3;
   private static final int SIZE_MINI = 4; // Taille minimum numéro
   private static final int SIZE_MAXI = 8;


   public void setScan(final Scanner scanner) {
      this.scan = scanner;
   }

   /**
    * Read keyboard inputs in menu
    * @return int 1,2 or 3 for valid input or -1 for invalid input
    */
   public int readSelection() {
      try {
         int input = Integer.parseInt(scan.nextLine());

         if (input >= MINI_INPUT_SELECTION && input <= MAXI_INPUT_SELECTION) {
            return input;
         } else {
            return -1;
         }
      } catch (IllegalArgumentException e) {
         LOGGER.error(ERROR_MESSAGE + "\r\nPlease enter valid number for proceeding further");
         return -1;
      }
   }

   /**
    * Read vehicle registration number inputs. 4-8 characters
    * @return vehicleRegNumber String | IllegalArgumentException
    */
   public String readVehicleRegistrationNumber() {
      try {
         String vehicleRegNumber = scan.nextLine();

         if (vehicleRegNumber == null || vehicleRegNumber.trim().length() < SIZE_MINI
                     || vehicleRegNumber.trim().length() > SIZE_MAXI
                     || !vehicleRegNumber.matches("^[0-9a-zA-Z]*$")) {
            throw new IllegalArgumentException("Invalid input provided");
         }
         return vehicleRegNumber;
      } catch (IllegalArgumentException e) {
         LOGGER.error(ERROR_MESSAGE
                     + "\r\nPlease enter a valid string"
                     + " for vehicle registration number");
         throw e;
      }
   }
}
