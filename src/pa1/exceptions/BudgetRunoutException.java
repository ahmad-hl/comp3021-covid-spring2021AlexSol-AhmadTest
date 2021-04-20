package pa1.exceptions;

import pa1.Player;

/**
 * Exception thrown when player does not have enough budget to afford a cost
 */
public class BudgetRunoutException extends Exception {

    private final Player player;
    /**
     * Initializes member variables
     *
     * @param player
     */
    public BudgetRunoutException(Player player) {
        this.player = player;
    }

    /**
     * @return an error message in the form:
     * "run out of budget %d budget"
     */
    @Override
    public String getMessage() {
        //TODO
        // return a string in the form: "run out of budget %d budget", you may want use String.format() like in lab 6
        return String.format("run out of budget %d ", player.getBudget());
    }

    @Override
    public String toString() {
        //TODO
        // return a string in the form: "BudgetRunoutException for player %s", you may want use String.format() like in lab 6
        return String.format("BudgetRunoutException for player %s", player.getName());
    }
}
