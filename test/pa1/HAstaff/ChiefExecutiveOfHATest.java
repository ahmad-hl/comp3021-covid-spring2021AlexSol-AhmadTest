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
    City ny;

    @BeforeEach
    void setUp() {
        System.out.println("initializing Method....");
        ceoStaff = new ChiefExecutiveOfHA(1,1,1);
        ny = new City(1,"ny", 2000, false, 800,0 );

        poorPlayer = new Player("U.S.", 1000, 100, 0);
    }

    @AfterEach
    void tearDown() {
        System.out.println("Cleaning Method up....");
    }

    @Test
    void getBonusPoints() {
        //bonus = leadership + experience = 2
        int bonus = ceoStaff.getBonusPoints();
        Assertions.assertEquals(2,bonus);
    }

    @Test
    void liftBanTravelTest() {
        //travelBanned = false
        Assertions.assertEquals(false, ny.isTravelBanned());
        ceoStaff.banTravel(poorPlayer,ny);
        //travelBanned = true
        Assertions.assertEquals(true, ny.isTravelBanned());
        //size = 1
        Assertions.assertEquals(1, poorPlayer.getContainTechniques().size());

        ceoStaff.liftTravelBan(poorPlayer,ny);
        //travelBanned = false
        Assertions.assertEquals(false, ny.isTravelBanned());
        //System.out.println(String.format("isBanned: %s", ny.isTravelBanned() ? "False" : "True"));
    }

    @Test
    void developMedicationFacility() throws NoEnoughBudgetException, BudgetRunoutException{
        //facilities = 0
        Assertions.assertEquals(0, ny.getMedicationFacilities());
        ceoStaff.developMedicationFacility(poorPlayer, ny);

        //facilities = 1
        Assertions.assertEquals(1, ny.getMedicationFacilities());

        //recovered cases = 100 [Constants.MEDICATION_FACILITY_CAPACITY]
        Assertions.assertEquals(100, ny.getRecoveredCases());
        //active cases = 800 - 100 = 700
        Assertions.assertEquals(700, ny.getActiveCases());

        // medicationLevel = 100 * 100/ 800 = 12 [.5]
        int mediLevel = ny.getRecoveredCases() * 100 / (ny.getActiveCases() + ny.getRecoveredCases());
        Assertions.assertEquals(mediLevel, poorPlayer.getContainTechniques().get(0).getMedication_level());
    }

    @Test
    void testToString() {
        ceoStaff.beginTurn();
        String toStr = String.format("ChiefExecutiveOfHA | READY, leadership %d, medicine %d, experience %d",ceoStaff.leadership, ceoStaff.medicine, ceoStaff.experience);
        Assertions.assertEquals(toStr, ceoStaff.toString());

        ceoStaff.endTurn();
        toStr = String.format("ChiefExecutiveOfHA | DONE, leadership %d, medicine %d, experience %d",ceoStaff.leadership, ceoStaff.medicine, ceoStaff.experience);
        Assertions.assertEquals(toStr, ceoStaff.toString());
    }
}