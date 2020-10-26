package com.parkit.parkingsystem.unit;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.parkit.parkingsystem.util.InputReaderUtil;

/**
 * Test for reader user input
 */
public class InputReaderUtilTest {

    private static InputReaderUtil inputReaderUtil;
    private static InputStream inputStream;
    private static Scanner scan;
    private static String input, vehicleRegNumber;

    @BeforeAll
    private static void setUp() {
        inputReaderUtil = new InputReaderUtil();
    }

    @Test
    @DisplayName("Selection utilisateur valide (1, 2, 3)")
    public void validInputUserSelection() {
        input = "2";
        inputStream = new ByteArrayInputStream((input).getBytes(StandardCharsets.UTF_8));
        scan = new Scanner(inputStream);
        inputReaderUtil.setScan(scan);
        assertThat(2).isEqualTo(inputReaderUtil.readSelection());
    }

    @Test
    @DisplayName("Selection utilisateur invalide (-1)")
    public void invalidInputUserSelection() {
        input = "-1";
        inputStream = new ByteArrayInputStream((input).getBytes(StandardCharsets.UTF_8));
        scan = new Scanner(inputStream);
        inputReaderUtil.setScan(scan);
        assertThat(-1).isEqualTo(inputReaderUtil.readSelection());
    }

    @Test
    @DisplayName("Selection utilisateur invalide (symbole)")
    public void invalidInputUserSelectionSymbol() {
        input = "$££$¤";
        inputStream = new ByteArrayInputStream((input).getBytes(StandardCharsets.UTF_8));
        scan = new Scanner(inputStream);
        inputReaderUtil.setScan(scan);
        assertThat(-1).isEqualTo(inputReaderUtil.readSelection());
    }

    @Test
    @DisplayName("Selection utilisateur invalide (12)")
    public void invalidInputUserSelectionMoreThanThree() {
        input = "12";
        inputStream = new ByteArrayInputStream((input).getBytes(StandardCharsets.UTF_8));
        scan = new Scanner(inputStream);
        inputReaderUtil.setScan(scan);
        assertThat(-1).isEqualTo(inputReaderUtil.readSelection());
    }

    @Test
    @DisplayName("Selection utilisateur invalide (lettre)")
    public void invalidInputUserSelectionLetter() {
        input = "z";
        inputStream = new ByteArrayInputStream((input).getBytes(StandardCharsets.UTF_8));
        scan = new Scanner(inputStream);
        inputReaderUtil.setScan(scan);
        assertThat(-1).isEqualTo(inputReaderUtil.readSelection());
    }

    @Test
    @DisplayName("Selection utilisateur invalide (lettres et symboles)")
    public void invalidInputUserSelectionLetterAndNumber() {
        input = "-15p*grtjhg$ầ-ç_(à-çè'(àç-è_";
        inputStream = new ByteArrayInputStream((input).getBytes(StandardCharsets.UTF_8));
        scan = new Scanner(inputStream);
        inputReaderUtil.setScan(scan);
        assertThat(-1).isEqualTo(inputReaderUtil.readSelection());
    }

    @Test
    @DisplayName("Numéro d'immatriculation valide minuscule")
    public void RegistrationNumberValidLowerCase() {
        vehicleRegNumber = "zorglub";
        inputStream = new ByteArrayInputStream((vehicleRegNumber).getBytes(StandardCharsets.UTF_8));
        scan = new Scanner(inputStream);
        inputReaderUtil.setScan(scan);
        inputReaderUtil.readVehicleRegistrationNumber();
        assertThat(vehicleRegNumber).hasSizeGreaterThan(4).hasSizeLessThan(8);
        assertThat(vehicleRegNumber).isEqualTo("zorglub");
    }

    @Test
    @DisplayName("Numéro d'immatriculation valide MAJUSCULE")
    public void RegistrationNumberValidUppercase() {
        vehicleRegNumber = "ZORGLUB";
        inputStream = new ByteArrayInputStream((vehicleRegNumber).getBytes(StandardCharsets.UTF_8));
        scan = new Scanner(inputStream);
        inputReaderUtil.setScan(scan);
        inputReaderUtil.readVehicleRegistrationNumber();
        assertThat(vehicleRegNumber).hasSizeGreaterThan(4).hasSizeLessThan(8);
        assertThat(vehicleRegNumber).isEqualTo("ZORGLUB");
    }

    @Test
    @DisplayName("Exception numéro d'immatriculation invalide")
    public void exceptionRegistrationNumberInvalid() {
        vehicleRegNumber = "AA";
        inputStream = new ByteArrayInputStream((vehicleRegNumber).getBytes(StandardCharsets.UTF_8));
        scan = new Scanner(inputStream);
        inputReaderUtil.setScan(scan);
        assertThatIllegalArgumentException().isThrownBy(() -> {
            inputReaderUtil.readVehicleRegistrationNumber();
        });
    }

    @Test
    @DisplayName("Exception numéro d'immatriculation invalide taille maxi")
    public void exceptionNumberMaxAllowSized() {
        vehicleRegNumber = "*MoreTHANAllowZORGLUBTESTTESTOKFHEOÏHGSIZED454ff";
        inputStream = new ByteArrayInputStream((vehicleRegNumber).getBytes(StandardCharsets.UTF_8));
        scan = new Scanner(inputStream);
        inputReaderUtil.setScan(scan);
        assertThatIllegalArgumentException().isThrownBy(() -> {
            inputReaderUtil.readVehicleRegistrationNumber();
        });
    }

    @Test
    @DisplayName("Exception numéro d'immatriculation invalide taille maxi + espace")
    public void exceptionNumberMaxAllowSizedAndSpace() {
        vehicleRegNumber = "*MORE than MAXI size 123*";
        inputStream = new ByteArrayInputStream(
                (vehicleRegNumber).getBytes(StandardCharsets.UTF_8));
        scan = new Scanner(inputStream);
        inputReaderUtil.setScan(scan);
        assertThatIllegalArgumentException().isThrownBy(() -> {
            inputReaderUtil.readVehicleRegistrationNumber();
        });
    }

    @Test
    @DisplayName("Exception numéro d'immatriculation invalide symbols")
    public void exceptionInputRegistrationSymbols() {
        vehicleRegNumber = "££££$$ùù%%%¤¤";
        inputStream = new ByteArrayInputStream((vehicleRegNumber).getBytes(StandardCharsets.UTF_8));
        scan = new Scanner(inputStream);
        inputReaderUtil.setScan(scan);
        assertThatIllegalArgumentException().isThrownBy(() -> {
            inputReaderUtil.readVehicleRegistrationNumber();
        });
    }

    @Test
    @DisplayName("Exception numéro d'immatriculation invalide nombre + espace")
    public void exceptionNumberMaxNumberAndSpace() {
        vehicleRegNumber = "            54£££¤¤";
        inputStream = new ByteArrayInputStream(
                (vehicleRegNumber).getBytes(StandardCharsets.UTF_8));
        scan = new Scanner(inputStream);
        inputReaderUtil.setScan(scan);
        assertThatIllegalArgumentException().isThrownBy(() -> {
            inputReaderUtil.readVehicleRegistrationNumber();
        });
    }
}