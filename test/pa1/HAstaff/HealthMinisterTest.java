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
    void testDevelopMedicationFacility() {
        //facilities = 0
        Assertions.assertEquals(0, ny.getMedicationFacilities());
        try {
            ministerStaff.developMedicationFacility(poorPlayer, ny);
        } catch (NoEnoughBudgetException e) {
            e.printStackTrace();
        } catch (BudgetRunoutException e) {
            e.printStackTrace();
        }

        //budget= 1000-500=500 [Constants.MEDICATION_FACILITY_COST=500]
        Assertions.assertEquals(500, poorPlayer.getBudget());

        //active cases = 800 - 100 = 700
        Assertions.assertEquals(700, ny.getActiveCases());

        // medicationLevel = 100 * 100/ 800 = 12 [.5]
        int mediLevel = ny.getRecoveredCases() * 100 / (ny.getActiveCases() + ny.getRecoveredCases());
        Assertions.assertEquals(mediLevel, poorPlayer.getContainTechniques().get(0).getMedication_level());

        // points = 3
        Assertions.assertEquals(3, poorPlayer.getPoints());
    }

    @Test
    void testBuildMasksFactory() {
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
    void testBuild_UpgradeFMaskQuality() {
        try {
            ministerStaff.upgradeFMaskQuality(poorPlayer, ny);
            // points = 0
            Assertions.assertEquals(0, poorPlayer.getPoints());
            //No containment techniques, size=0 (Must NOT add Facemask obj if not exists )
            Assertions.assertEquals(0, poorPlayer.getContainTechniques().size());


            ministerStaff.buildMasksFactory(poorPlayer, ny);
            // protectionLevel = 30 [MASK_PROTECTION_Percentage=30]
            Assertions.assertEquals(30, poorPlayer.getContainTechniques().get(0).getProtection_level());
            // points = 3
            Assertions.assertEquals(3, poorPlayer.getPoints());

            ministerStaff.upgradeFMaskQuality(poorPlayer, ny);
            // points = 3
            Assertions.assertEquals(3, poorPlayer.getPoints());
            //size=1
            Assertions.assertEquals(1, poorPlayer.getContainTechniques().size());
            // protectionLevel = 50 [UPGRADE_MASK_PROTECTION_Percentage=50]
            Assertions.assertEquals(50, poorPlayer.getContainTechniques().get(0).getProtection_level());

        } catch (NoEnoughBudgetException e) {
            e.printStackTrace();
        } catch (BudgetRunoutException e) {
            e.printStackTrace();
        }

    }

    @Test
    void testDevelop_UpgradeVaccine() {
        try {
            ministerStaff.developVaccine(poorPlayer, ny);
            // points = 0
            Assertions.assertEquals(0, poorPlayer.getPoints());
            //budget= 1000-50=950 [Constants.DEVELOP_VACCINE_COST=50]
            Assertions.assertEquals(950, poorPlayer.getBudget());
            // vaccination level = 50 [DEVELOP_VACCINE_Percentage=50]
            Assertions.assertEquals(50, poorPlayer.getContainTechniques().get(0).getVaccination_level());

            ministerStaff.upgradeVaccine(poorPlayer, ny);
            // points = 0
            Assertions.assertEquals(0, poorPlayer.getPoints());
            //budget= 950-50=900 [Constants.UPGRADE_VACCINE_COST=50]
            Assertions.assertEquals(900, poorPlayer.getBudget());
            // vaccination level = 100 [UPGRADE_VACCINE_Percentage=50]
            Assertions.assertEquals(100, poorPlayer.getContainTechniques().get(0).getVaccination_level());

        } catch (NoEnoughBudgetException e) {
            e.printStackTrace();
        } catch (BudgetRunoutException e) {
            e.printStackTrace();
        }
    }

}