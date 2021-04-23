package pa1.HAstaff;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pa1.City;
import pa1.Player;
import pa1.exceptions.BudgetRunoutException;
import pa1.exceptions.MedicalException;
import pa1.exceptions.NoEnoughBudgetException;
import pa1.util.Constants;

import static org.junit.jupiter.api.Assertions.*;

class EpidemiologistTest {

    Epidemiologist epiStaff;
    Player poorPlayer;
    City newyork;

    @BeforeEach
    void setUp() {
        System.out.println("initializing Epidemiologist....");
        epiStaff = new Epidemiologist(1,1,1);
        newyork = new City(2,"ny", 2000, false, 800,0 );
        poorPlayer = new Player("U.S.", 1000, 100, 0);
    }

    @AfterEach
    void tearDown() {
        System.out.println("Cleaning up Epidemiologist....");
    }

    @Test
    void testBuild_UpgradeFMask() {
        try {
            epiStaff.upgradeFMaskQuality(poorPlayer, newyork);

            //No containment techniques (Must NOT add Facemask obj if not exists )
            Assertions.assertEquals(0, poorPlayer.getContainTechniques().size());
            // points = 2
            Assertions.assertEquals(2, poorPlayer.getPoints());

            //Will set protection level to 30  [Constants.MASK_PROTECTION_Percentage]
            epiStaff.buildMasksFactory(poorPlayer, newyork);

            // points = 2
            Assertions.assertEquals(2, poorPlayer.getPoints());
            // protection level = 30 [Constants.MASK_PROTECTION_Percentage=30]
            Assertions.assertEquals(30, poorPlayer.getContainTechniques().get(0).getProtection_level());

            epiStaff.upgradeFMaskQuality(poorPlayer, newyork);
            // protection level = 30 + 20 [Constants.UPGRADE_MASK_PROTECTION_Percentage =20]
            Assertions.assertEquals(50, poorPlayer.getContainTechniques().get(0).getProtection_level());
            // containments size = 1
            Assertions.assertEquals(1, poorPlayer.getContainTechniques().size());
        } catch (NoEnoughBudgetException e) {
            e.printStackTrace();
        } catch (BudgetRunoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDevelop_UpgradeVaccine() {
        try {
            epiStaff.upgradeVaccine(poorPlayer, newyork);
            // points = 2
            Assertions.assertEquals(2, poorPlayer.getPoints());
            //No containment techniques (Must NOT add Facemask obj if not exists )
            Assertions.assertEquals(0, poorPlayer.getContainTechniques().size());


            //Will set vaccination level to 50  [Constants.DEVELOP_VACCINE_Percentage]
            epiStaff.developVaccine(poorPlayer, newyork);
            // vaccination level = 50
            Assertions.assertEquals(50, poorPlayer.getContainTechniques().get(0).getVaccination_level());

            // vaccination level = 50 + 50  [Constants.UPGRADE_VACCINE_Percentage=50]
            epiStaff.upgradeVaccine(poorPlayer, newyork);
            Assertions.assertEquals(100, poorPlayer.getContainTechniques().get(0).getVaccination_level());
            // points = 6
            Assertions.assertEquals(6, poorPlayer.getPoints());
        } catch (NoEnoughBudgetException e) {
            e.printStackTrace();
        } catch (BudgetRunoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testBudgetExceptions() {
        poorPlayer.decreaseBudget(970);
        assertEquals(30, poorPlayer.getBudget());

        Exception exception = assertThrows(BudgetRunoutException.class, () -> {
            epiStaff.developVaccine(poorPlayer, newyork);
        });

        String expectedMessage = "run out of budget 30 ";

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}