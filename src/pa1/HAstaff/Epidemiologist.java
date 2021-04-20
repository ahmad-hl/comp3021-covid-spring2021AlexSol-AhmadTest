package pa1.HAstaff;


import pa1.City;
import pa1.Player;
import pa1.exceptions.BudgetRunoutException;
import pa1.exceptions.NoEnoughBudgetException;

public class Epidemiologist extends HealthAuthorityStaff {

    /**
     * Initialize Epidemiologist
     *call the super class constructor
     *
     * @param leadership
     * @param medicine
     * @param experience
     */
    public Epidemiologist(int leadership, int medicine, int experience) {
        super(leadership, medicine, experience);
    }

    /**
     * compute bonus
     * Epidemiologist bonus is based on summation of medicine, knowledge and experience
     * <p>
     * HINT:
     *
     * @return int
     */
    @Override
    protected int getBonusPoints() {
        return medicine + experience;
    }

    /**
     * develop a vaccine
     * 1. Call the super class developVaccine
     * 2. add points according to bonus
     * <p>
     * HINT:
     *
     * @throws NoEnoughBudgetException
     * @throws BudgetRunoutException
     */
    @Override
    public void developVaccine(Player player, City city) throws NoEnoughBudgetException, BudgetRunoutException {
        //TODO
        // note: 1. you need to call the developVaccine() method of the super class
        //          see whether the vaccine is developed correctly
        //          (i.e. superclass developVaccine() returns true).
        //       2. here it is different from lab 6,  we assume developVaccine() is always successful (if unsuccessful
        //          we will throw an exception instead of calling this method, this is different than lab 6).
        //          so you will always need to call the addPoints() method
        //          of the player object here. You can get the amount of bonus point to add
        //          by calling getBonusPoints() method of this Epidemiologist object
        super.developVaccine(player, city);
        player.addPoints(getBonusPoints());
    }

    /**
     * upgrade a vaccine
     * 1. Call the super class upgradeVaccine
     * 2. add points according to bonus
     * <p>
     * HINT:
     *
     * @throws NoEnoughBudgetException
     * @throws BudgetRunoutException
     */
    @Override
    public void upgradeVaccine(Player player, City city) throws NoEnoughBudgetException, BudgetRunoutException {
        //TODO
        // note: 1. call upgradeVaccine() of the super class, pass it with player and city
        //       2. call the addPoints() method of the player object here. You can get the
        //           amount of bonus point to add by calling getBonusPoints() method of
        //           this Epidemiologist object
        super.upgradeVaccine(player, city);
        player.addPoints(getBonusPoints());
    }

    /**
     * upgrade the quality of the face masks
     * 1. Call the super class upgradeFMaskQuality
     * 2. add points according to bonus
     * <p>
     * HINT:
     *
     * @throws NoEnoughBudgetException
     * @throws BudgetRunoutException
     */
    @Override
    public void upgradeFMaskQuality(Player player, City city) throws NoEnoughBudgetException, BudgetRunoutException {
        //TODO
        // note: 1. call upgradeFMaskQuality() of the super class, pass it with player and city
        //       2. call the addPoints() method of the player object here. You can get the
        //           amount of bonus point to add by calling getBonusPoints() method of
        //           this Epidemiologist object
        super.upgradeFMaskQuality(player, city);
        player.addPoints(getBonusPoints());
    }

    /**
     * Example string representation:
     * "Epidemiologist | READY, leadership, medicine, experience" - when isReady() == true
     * "Epidemiologist | DONE, leadership, medicine, experience" - when isReady() == false
     *
     * @return string representation of this object
     */
    @Override
    public String toString() {
        String toStr = String.format("Epidemiologist | %s, leadership %d, medicine %d, experience %d", isReady() ? "READY" : "DONE",leadership, medicine, experience);
        return toStr;
    }
}
