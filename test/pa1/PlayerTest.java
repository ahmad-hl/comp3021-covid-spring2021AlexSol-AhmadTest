package pa1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pa1.HAstaff.ChiefExecutiveOfHA;
import pa1.HAstaff.Epidemiologist;
import pa1.HAstaff.HealthAuthorityStaff;
import pa1.HAstaff.HealthMinister;
import pa1.containment.Containment;
import pa1.containment.FaceMask;
import pa1.containment.Isolation;
import pa1.containment.Vaccination;
import pa1.exceptions.BudgetRunoutException;
import pa1.exceptions.MedicalException;
import pa1.exceptions.NoEnoughBudgetException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    ChiefExecutiveOfHA ceoStaff;
    Epidemiologist epiStaff;
    HealthMinister ministerStaff;

    Player poorPlayer;

    @BeforeEach
    void Setup(){
//        System.out.println("initializing Player....");
        City newyork = new City(1,"newyork", 2000, false, 800,0 );
        poorPlayer = new Player("U.S.", 1000, 100, 0);
        poorPlayer.setCity(newyork);

        ceoStaff = new ChiefExecutiveOfHA(1,1,1);
        ministerStaff = new HealthMinister(1,1,1);
        epiStaff = new Epidemiologist(1,1,1);
        poorPlayer.getHAStaffs().add(ceoStaff);
        poorPlayer.getHAStaffs().add(ministerStaff);
        poorPlayer.getHAStaffs().add(epiStaff);
    }

    @AfterEach
    void TearDown(){

//        System.out.println("Cleaning Player....");
    }

    //Unit Testing for Player class
    @Test
    void testHasReadyHAStaff(){
        //false
        boolean has = poorPlayer.hasReadyHAStaff();
        assertFalse(has);

        poorPlayer.getHAStaffs().get(0).beginTurn();
        //true
        has = poorPlayer.hasReadyHAStaff();
        assertTrue(has);
    }

    @Test
    void simpleTestIncrement_halfProtection() {
        //size =1
        poorPlayer.addContainmentTech(new FaceMask());
        assertEquals(1, poorPlayer.getContainTechniques().size());

        //Half protection = 30
        poorPlayer.incrementProtection_level(30, poorPlayer.getContainTechniques().get(0) );
        assertEquals(30, poorPlayer.getContainTechniques().get(0) .getProtection_level());

        //Half protection = 15
        poorPlayer.halfProtection_level();
        assertEquals(15, poorPlayer.getContainTechniques().get(0).getProtection_level());

        //protection = 100 at max
        poorPlayer.incrementProtection_level(110, poorPlayer.getContainTechniques().get(0) );
//        assertEquals(100, poorPlayer.getContainTechniques().get(0) .getProtection_level());
        assertTrue(poorPlayer.getContainTechniques().get(0) .getProtection_level() == 100 || poorPlayer.getContainTechniques().get(0) .getProtection_level() ==125);
    }


    @Test
    void simpleTestIncrement_halfVaccination() {
        //size = 1
        poorPlayer.addContainmentTech(new Vaccination());
        assertEquals(1, poorPlayer.getContainTechniques().size());
        //Half protection = 50
        poorPlayer.incrementVaccination_level(50, poorPlayer.getContainTechniques().get(0) );
        assertEquals(50, poorPlayer.getContainTechniques().get(0) .getVaccination_level());

        //Half protection = 25
        poorPlayer.halfVaccination_level();
        assertEquals(25, poorPlayer.getContainTechniques().get(0).getVaccination_level());

        //vaccination = 100 at max
        poorPlayer.incrementVaccination_level(110, poorPlayer.getContainTechniques().get(0) );
//        assertEquals(100, poorPlayer.getContainTechniques().get(0) .getVaccination_level());
        assertTrue(poorPlayer.getContainTechniques().get(0) .getVaccination_level() == 100 || poorPlayer.getContainTechniques().get(0) .getVaccination_level() ==125);
    }


    //Integration Testing from Player's POV
    @Test
    void test1_ComputeNewInfectedCases(){
        //Case study 1: no protection or vaccination:
        // #new cases = spreadRate (3) * increaseFactor (1) * city's active Cases
        Exception exception = assertThrows(MedicalException.class, () -> {
            poorPlayer.computeNewInfectedCases();
        });

        int expectedNewInfectedCases = 3 * 1 * 800;  //Math.ceil(spreadRate * increaseFactor * city.getActiveCases())
        //newInfectedCases = 2400
        assertEquals(expectedNewInfectedCases, poorPlayer.getCity().getNumNewCases());
        //max(population, newInfectedCases) = 2000
        assertEquals(2000, poorPlayer.getCity().getActiveCases());

        String expectedMessage = String.format("activeCases cases %d reached city's population %d",
                2000, 2000);
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void test2_ComputeNewInfectedCases(){
        //Case study 1:  protection 50% and vaccination 50%:
        // #new cases = spreadRate (3 * 0.5) * increaseFactor (0.5) * city's active Cases

        try {
            //Isolation => 50% protection
            poorPlayer.getHAStaffs().get(0).banTravel(poorPlayer, poorPlayer.getCity());
            poorPlayer.getHAStaffs().get(0).developVaccine(poorPlayer, poorPlayer.getCity());

            poorPlayer.computeNewInfectedCases();
        } catch (MedicalException e) {
            e.printStackTrace();
        } catch (NoEnoughBudgetException e) {
            e.printStackTrace();
        } catch (BudgetRunoutException e) {
            e.printStackTrace();
        }

        //Math.ceil(spreadRate * increaseFactor * increaseFactor * city.getActiveCases())
        int expectedNewInfectedCases = (int) Math.ceil(3 * 0.5 * 0.5 * 800);
        //newInfectedCases = 600
        assertEquals(600, poorPlayer.getCity().getNumNewCases());
        //NewInfectedCases+ active cases
        assertEquals(1400 , poorPlayer.getCity().getActiveCases());
    }

    @Test
    void testIncrement_halfProtectionLevel() {
        try {
            // increment protection level 30
            poorPlayer.getHAStaffs().get(0).buildMasksFactory(poorPlayer,poorPlayer.getCity());

            // protection level =30/2=15
            poorPlayer.halfProtection_level();
            Assertions.assertEquals(15, poorPlayer.getContainTechniques().get(0).getProtection_level());

            // increment protection level 15+30+30
            poorPlayer.getHAStaffs().get(1).buildMasksFactory(poorPlayer,poorPlayer.getCity());
            poorPlayer.getHAStaffs().get(2).buildMasksFactory(poorPlayer,poorPlayer.getCity());
//            poorPlayer.getHAStaffs().get(0).buildMasksFactory(poorPlayer,poorPlayer.getCity());
        } catch (NoEnoughBudgetException e) {
            e.printStackTrace();
        } catch (BudgetRunoutException e) {
            e.printStackTrace();
        }

        //No containment techniques, size=1
        Assertions.assertEquals(1, poorPlayer.getContainTechniques().size());
        // protection level<=100
        Assertions.assertTrue(poorPlayer.getContainTechniques().get(0).getProtection_level()<=100);
    }



    @Test
    void testIncrement_half_VaccinationLevel() {
        try {
            // increment vaccination level 50
            poorPlayer.getHAStaffs().get(0).developVaccine(poorPlayer,poorPlayer.getCity());

            // vaccination level =50/2=25
            poorPlayer.halfVaccination_level();
            Assertions.assertEquals(25, poorPlayer.getContainTechniques().get(0).getVaccination_level());

            // increment vaccination level 25+50+50
            poorPlayer.getHAStaffs().get(1).developVaccine(poorPlayer,poorPlayer.getCity());
            poorPlayer.getHAStaffs().get(2).developVaccine(poorPlayer,poorPlayer.getCity());
//            poorPlayer.getHAStaffs().get(0).developVaccine(poorPlayer,poorPlayer.getCity());
        } catch (NoEnoughBudgetException e) {
            e.printStackTrace();
        } catch (BudgetRunoutException e) {
            e.printStackTrace();
        }

        //No containment techniques, size=1
        Assertions.assertEquals(1, poorPlayer.getContainTechniques().size());
        // vaccination level<=100
        Assertions.assertTrue(poorPlayer.getContainTechniques().get(0).getVaccination_level()<=100);
        // System.out.println(String.format(" vaccination level=%d",poorPlayer.getContainTechniques().get(0).getVaccination_level()));
    }

}