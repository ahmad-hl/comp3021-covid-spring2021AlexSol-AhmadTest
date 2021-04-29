package pa1.HAstaff;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pa1.City;
import pa1.Player;
import pa1.exceptions.BudgetRunoutException;
import pa1.exceptions.NoEnoughBudgetException;
import pa1.util.Constants;

import static org.junit.jupiter.api.Assertions.*;

class ChiefExecutiveOfHATest {

    ChiefExecutiveOfHA ceoStaff;
    Player poorPlayer;

    @BeforeEach
    void setUp() {
//        System.out.println("initializing ChiefExecutiveOfHA....");
        ceoStaff = new ChiefExecutiveOfHA(1,1,1);
        City ny = new City(1,"newyork", 2000, false, 800,0 );

        poorPlayer = new Player("U.S.", 1000, 100, 0);
        poorPlayer.setCity(ny);
    }

    @AfterEach
    void tearDown() {
//        System.out.println("Cleaning ChiefExecutiveOfHA up....");
    }

    @Test
    void developMedicationFacility() throws NoEnoughBudgetException, BudgetRunoutException{
        //facilities = 0
        Assertions.assertEquals(0, poorPlayer.getCity().getMedicationFacilities());
        ceoStaff.developMedicationFacility(poorPlayer, poorPlayer.getCity());

        //facilities = 1
        Assertions.assertEquals(1, poorPlayer.getCity().getMedicationFacilities());

        //recovered cases = 100 [Constants.MEDICATION_FACILITY_CAPACITY]
        Assertions.assertEquals(100, poorPlayer.getCity().getRecoveredCases());
        //active cases = 800 - 100 = 700
        Assertions.assertEquals(700, poorPlayer.getCity().getActiveCases());

        // medicationLevel = 100 * 100/ 800 = 12 [.5]
        int mediLevel = poorPlayer.getCity().getRecoveredCases() * 100 / (poorPlayer.getCity().getActiveCases() + poorPlayer.getCity().getRecoveredCases());
        Assertions.assertEquals(mediLevel, poorPlayer.getContainTechniques().get(0).getMedication_level());

        // points = 0
        Assertions.assertEquals(0, poorPlayer.getPoints());
    }

    @Test
    void liftBanTravelTest() {
        ceoStaff.banTravel(poorPlayer,poorPlayer.getCity());
        //travelBanned = true
        Assertions.assertTrue(poorPlayer.getCity().isTravelBanned());
        //size = 1
        Assertions.assertEquals(1, poorPlayer.getContainTechniques().size());
        //protection level = 50
        Assertions.assertEquals(50, poorPlayer.getContainTechniques().get(0).getProtection_level());
        // points = 2
        Assertions.assertEquals(2, poorPlayer.getPoints());

        ceoStaff.liftTravelBan(poorPlayer,poorPlayer.getCity());
        //travelBanned = false
        Assertions.assertFalse(poorPlayer.getCity().isTravelBanned());
        //size = 0
        Assertions.assertEquals(0, poorPlayer.getContainTechniques().size());
        //System.out.println(String.format("isBanned: %s", ny.isTravelBanned() ? "False" : "True"));
    }


}