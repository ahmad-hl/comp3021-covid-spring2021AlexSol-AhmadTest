package pa1.HAstaff;

import pa1.City;
import pa1.Player;
import pa1.exceptions.BudgetRunoutException;
import pa1.exceptions.NoEnoughBudgetException;


public class HealthMinister extends HealthAuthorityStaff {

    /**
     * Initialize HealthMinister
     * call the super class constructor
     *
     * @param leadership
     * @param medicine
     * @param experience
     */
    public HealthMinister(int leadership, int medicine, int experience) {
        super(leadership, medicine, experience);
    }

    /**
     * compute bonus
     * HealthMinister bonus is based on  summation of leadership, medicine knowledge and experience
     * <p>
     * HINT:
     *
     * @return int
     */
    @Override
    protected int getBonusPoints() {
        return leadership + medicine + experience;
    }

    /**
     * develop a Medication Facility
     * 1. Call the super class developMedicationFacility
     * 2. add points according to bonus
     * <p>
     * HINT:
     *
     * @throws NoEnoughBudgetException
     * @throws BudgetRunoutException
     */
    @Override
    public void developMedicationFacility(Player player, City city) throws NoEnoughBudgetException, BudgetRunoutException {
        super.developMedicationFacility(player, city);
        player.addPoints(getBonusPoints());
    }

    /**
     * build a Masks Factory
     * 1. Call the super class buildMasksFactory
     * 2. add points according to bonus
     * <p>
     * HINT:
     *
     * @throws NoEnoughBudgetException
     * @throws BudgetRunoutException
     */
    @Override
    public void buildMasksFactory(Player player, City city) throws NoEnoughBudgetException, BudgetRunoutException {
        super.buildMasksFactory(player, city);
        player.addPoints(getBonusPoints());
    }


    /**
     * Example string representation:
     * "HealthMinister | READY, leadership, medicine, experience" - when isReady() == true
     * "HealthMinister | DONE, leadership, medicine, experience" - when isReady() == false
     *
     * @return string representation of this object
     */
    @Override
    public String toString() {
        String toStr = String.format("HealthMinister | %s, leadership %d, medicine %d, experience %d", isReady() ? "READY" : "DONE",leadership, medicine, experience);
        return toStr;
    }
}

