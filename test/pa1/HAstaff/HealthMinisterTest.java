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

class HealthMinisterTest {

    HealthMinister ministerStaff;
    Player poorPlayer;
    City ny;

    @BeforeEach
    void setUp() {
        System.out.println("initializing HealthMinister....");
        ministerStaff = new HealthMinister(1,1,1);
        ny = new City(2,"ny", 2000, false, 800,0 );
        poorPlayer = new Player("U.S.", 1000, 100, 0);
    }

    @AfterEach
    void tearDown() {
        System.out.println("Cleaning up HealthMinister....");
    }

    @Test
    void getBonusPoints() {
        //bonus = leadership + medicine + experience = 3
        int bonus = ministerStaff.getBonusPoints();
        Assertions.assertEquals(3,bonus);
    }

    @Test
    void developMedicationFacility() {
        //facilities = 0
        Assertions.assertEquals(0, ny.getMedicationFacilities());
        try {
            ministerStaff.developMedicationFacility(poorPlayer, ny);
        } catch (NoEnoughBudgetException e) {
            e.printStackTrace();
        } catch (BudgetRunoutException e) {
            e.printStackTrace();
        }

        //facilities = 1
        Assertions.assertEquals(1, ny.getMedicationFacilities());

        //recovered cases = 100 [Constants.MEDICATION_FACILITY_CAPACITY]
        Assertions.assertEquals(100, ny.getRecoveredCases());
        //active cases = 800 - 100 = 700
        Assertions.assertEquals(700, ny.getActiveCases());

        // medicationLevel = 100 * 100/ 800 = 12 [.5]
        int mediLevel = ny.getRecoveredCases() * 100 / (ny.getActiveCases() + ny.getRecoveredCases());
        Assertions.assertEquals(mediLevel, poorPlayer.getContainTechniques().get(0).getMedication_level());

        // points = 3
        Assertions.assertEquals(3, poorPlayer.getPoints());
    }

    @Test
    void buildMasksFactory() {
        //budget = 1000
        Assertions.assertEquals(1000, poorPlayer.getBudget());

        try {
            ministerStaff.buildMasksFactory(poorPlayer, ny);
        } catch (NoEnoughBudgetException e) {
            e.printStackTrace();
        } catch (BudgetRunoutException e) {
            e.printStackTrace();
        }

        //budget= 1000-100=900 [Constants.BUILD_MASK_FACTORY_COST=100]
        Assertions.assertEquals(900, poorPlayer.getBudget());

        // protectionLevel = 30 [MASK_PROTECTION_Percentage=30]
        Assertions.assertEquals(30, poorPlayer.getContainTechniques().get(0).getProtection_level());

        // points = 3
        Assertions.assertEquals(3, poorPlayer.getPoints());
    }

    @Test
    void upgradeFMaskQuality() {
        try {
            ministerStaff.upgradeFMaskQuality(poorPlayer, ny);
        } catch (NoEnoughBudgetException e) {
            e.printStackTrace();
        } catch (BudgetRunoutException e) {
            e.printStackTrace();
        }
        // points = 0
        Assertions.assertEquals(0, poorPlayer.getPoints());
    }

    @Test
    void developVaccine() {
        try {
            ministerStaff.developVaccine(poorPlayer, ny);
        } catch (NoEnoughBudgetException e) {
            e.printStackTrace();
        } catch (BudgetRunoutException e) {
            e.printStackTrace();
        }
        // points = 0
        Assertions.assertEquals(0, poorPlayer.getPoints());
    }

    @Test
    void upgradeVaccine() {
        try {
            ministerStaff.upgradeVaccine(poorPlayer, ny);
        } catch (NoEnoughBudgetException e) {
            e.printStackTrace();
        } catch (BudgetRunoutException e) {
            e.printStackTrace();
        }
        // points = 0
        Assertions.assertEquals(0, poorPlayer.getPoints());
    }

    @Test
    void liftBanTravelTest() {
        //travelBanned = false
        Assertions.assertEquals(false, ny.isTravelBanned());
        ministerStaff.banTravel(poorPlayer,ny);
        //travelBanned = true
        Assertions.assertEquals(true, ny.isTravelBanned());
        //size = 1
        Assertions.assertEquals(1, poorPlayer.getContainTechniques().size());

        ministerStaff.liftTravelBan(poorPlayer,ny);
        //travelBanned = false
        Assertions.assertEquals(false, ny.isTravelBanned());
        // points = 0
        Assertions.assertEquals(0, poorPlayer.getPoints());
    }
}