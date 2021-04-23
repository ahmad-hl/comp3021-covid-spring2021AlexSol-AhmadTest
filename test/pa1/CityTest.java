package pa1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pa1.HAstaff.Epidemiologist;
import pa1.exceptions.MedicalException;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    City newyork;

    @BeforeEach
    void setUp() {
        System.out.println("initializing City....");
        newyork = new City(2,"ny", 2000, false, 800,0 );
    }

    @AfterEach
    void tearDown() {
        System.out.println("Cleaning City....");
    }

    @Test
    void decreaseMedicationFacility() {
    }

    @Test
    void testIncreaseActiveCases() {
        Exception exception = assertThrows(MedicalException.class, () -> {
            newyork.increaseActiveCases(1300);
        });

        String expectedMessage = String.format("activeCases cases %d reached city's population %d",
                2000, 2000);
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDecreaseActiveCases() {
        newyork.decreaseActiveCases(2100);
        assertEquals(0, newyork.getActiveCases());
        assertEquals(2000, newyork.getRecoveredCases());
    }

    @Test
    void testDecreaseMedicationFacility() {
        newyork.decreaseMedicationFacility();
        assertEquals(0, newyork.getMedicationFacilities());
    }
}