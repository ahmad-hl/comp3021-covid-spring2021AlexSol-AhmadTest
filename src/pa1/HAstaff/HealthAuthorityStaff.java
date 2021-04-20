package pa1.HAstaff;


import pa1.City;
import pa1.Player;
import pa1.containment.*;
import pa1.exceptions.BudgetRunoutException;
import pa1.exceptions.NoEnoughBudgetException;
import pa1.util.Constants;

/**
 * An abstract class that represents a health authority staff (haStaffs) in the game.
 * All actions in the game are done through haStaffs.
 * Therefore this class will contain the implementations of the actions to take in order to contain COVID in the game.
 */
public abstract class HealthAuthorityStaff {

    // Attributes
    protected final int leadership, medicine, experience;
    private boolean isReady = false;

    /**
     * Initializes the attributes of a HAstaff
     * @param leadership
     * @param medicine
     * @param experience
     */
    public HealthAuthorityStaff(int leadership, int medicine, int experience) {
        this.leadership = leadership;
        this.medicine = medicine;
        this.experience = experience;
    }

    /**
     * @return Whether or not the minister is ready
     */
    public boolean isReady() {
        //TODO
        // just return isReady
        return isReady;
    }

    /**
     * Changes isReady to true
     */
    public void beginTurn() {
        //TODO
        // set isReady to true
        isReady = true;
    }

    /**
     * Changes isReady to false
     */
    public void endTurn() {
        //TODO
        // set isReady to false
        isReady = false;
    }

    protected int getBonusPoints(){
        return 0;
    }

    /**
     * Develop a medication facility in the city
     * 1. Get the cost of developing a facility
     * 2. if the player doesn't have enough budget, throw an exception
     *    otherwise:
     * 3. decrease the player's budget by the cost
     * 4. Increment the city's medication facilities by one
     * 5. set medication level to: recovered * 100/ (active + recovered)
     * <p>
     * HINT:
     *  define a class of constant final variable to include the costs, percentages, ..etc
     * @param player
     * @param city
     * @throws NoEnoughBudgetException
     * @throws BudgetRunoutException
     */
    public void developMedicationFacility(Player player, City city) throws NoEnoughBudgetException, BudgetRunoutException {
        //TODO
        // note: 1. you need to first check whether the current budget of the player is >= to the min budget allowed
        //          in order to *sustain the player's city*. Only when the current budget is bigger than that amount, you will continue,
        //          otherwise you will throw BudgetRunoutException.
        //          - To get the current budget of the player, you can use the getBudget() method of the player object
        //          - the min budget allowed (before developing a medication facility) is the constant MIN_ALLOWED_BUDGET, in the Constants class
        //       2. then you need to check if the budget of the player is enough for building a medication facility (the cost is in
        //           Constants.MEDICATION_FACILITY_COST).
        //             - if the budget is not enough, throws NoEnoughBudgetException
        //             - Otherwise:
        //                  - you subtract the medication facility cost from the budget of the player (player.decreaseBudget()),
        //                  - add one new medication facility to the city object (through addMedicationFacilities() method of the city object),
        //                  - and then decrease the active cases of the city using the decreaseActiveCases() method of the city (we assume that a new medical
        //                  facility will immediately cure a number of active patients). The number of patients get cured immediately
        //                  is the same as the constant Constants.MEDICATION_FACILITY_CAPACITY). To make it simple, for every medical facility built
        //                  we decrease the number of active cases once only for the whole duration of the game (but not decrease the active cases in every turn).
        //                  - *different from the labs*, you will need to check if the "Treatment" class object that corresponding to the "treatement" offered by medication facility is already present.
        //                  You can do that by checking the elements of the ArrayList of the player using "player.getContainTechniques()", and check to see if there is an element that is an instance of
        //                  "Treatment". If it does not exist create a new Treatment object using the default constructor of "Treatment" class. add this object to the player using "addContainmentTech()"
        //                  method of the player. If a "Treatment" object already exists (or you have just added it), use this object to set the new medication level of the player through
        //                  setMedication_level(), you should set the new medication level to (city.getRecoveredCases() * 100) / (city.getActiveCases() + city.getRecoveredCases()).
        //                  You may find the indexOf() method of the player's containTechniques ArrayList useful to retrieve the "Treatment" object.
        if (player.getBudget()<Constants.MIN_ALLOWED_BUDGET)
            throw new BudgetRunoutException(player);

        //update the player's budget if can afford the action
        int facilityCost = Constants.MEDICATION_FACILITY_COST;
        boolean cantDevelopFacility = facilityCost > player.getBudget();
        if (cantDevelopFacility) throw new NoEnoughBudgetException(player, facilityCost);
        player.decreaseBudget(facilityCost);
        city.addMedicationFacilities();
        city.decreaseActiveCases(Constants.MEDICATION_FACILITY_CAPACITY );

        //Add Treatment to the player's containment techniques
        boolean alreadyExists = false;
        for (Containment cont:player.getContainTechniques()) {
            if (cont instanceof Treatment){
                alreadyExists = true;
                break;
            }
        }

        if (!alreadyExists){
            Treatment treat = new Treatment();
            player.addContainmentTech(treat);
        }

        //update medication level: medication level = #med facilities * capacity * 100/ #recovered cases
        for (Containment cont:player.getContainTechniques()) {
            if (cont instanceof Treatment) {
                int index = player.getContainTechniques().indexOf(cont);
                player.getContainTechniques().get(index).setMedication_level((city.getRecoveredCases() * 100) / (city.getActiveCases() + city.getRecoveredCases()));
            }
        }

    }


    /**
     * Build a mask factory in the city
     * 1. Get the cost of building masks factory
     * 2. if the player doesn't have enough budget, throw an exception
     *      otherwise:
     * 3. decrease the player's budget according to the cost
     * 4. if not exists, add facemasks to the player's containment techniques
     * 5. increment the protection level by Constants.MASK_PROTECTION_Percentage
     * @param player
     * @param city
     * @throws NoEnoughBudgetException
     * @throws BudgetRunoutException
     */
    public void buildMasksFactory(Player player, City city) throws NoEnoughBudgetException, BudgetRunoutException {
        //TODO
        // note: 1. you need to first check whether the current budget of the player is >= to the min budget allowed
        //          in order to sustain the player's city. Only when the current budget is bigger than that amount, you will continue,
        //          otherwise you will throw BudgetRunoutException.
        //          - To get the current budget of the player, you can use the getBudget() method of the player object
        //          - the min budget allowed (before developing a mask factory) is the constant MIN_ALLOWED_BUDGET, in the Constants class
        //       2. then you need to check if the budget of the player is enough for building a mask factory (the cost is in
        //           Constants.BUILD_MASK_FACTORY_COST).
        //           - if the budget is not enough, throw NoEnoughBudgetException
        //           - Otherwise:
        //             - you subtract the mask factory cost from the budget of the player (player.decreaseBudget()),
        //             - *different from the labs*, you will need to check if the "FaceMask" class object that corresponding to the virus containment technique offered by the face masks is already present.
        //             You can do that by checking the elements of the ArrayList of the player using "player.getContainTechniques()", and check to see if there is an element that is an instance of
        //             "FaceMask". If it does not exist create a new FaceMask object using the default constructor of "FaceMask" class, add this object to the player using "addContainmentTech()"
        //              method of the player. If a "FaceMask" object already exists (or you have just added it), use this object to increase the protection level of the player (as more people
        //              could be protected by masks now). To do that you need to use the player's incrementProtection_level() method. Assume the protected level is incremented by the constant Constants.MASK_PROTECTION_Percentage.
        //              Remember to pass the "FaceMask" containment object to the player's incrementProtection_level() method as the second argument when you call it
        if (player.getBudget()<Constants.MIN_ALLOWED_BUDGET)
            throw new BudgetRunoutException(player);

        //update the player's budget if can afford the action
        int maskFactoryCost = Constants.BUILD_MASK_FACTORY_COST;
        boolean cantBuildMedFactory = maskFactoryCost > player.getBudget();
        if (cantBuildMedFactory) throw new NoEnoughBudgetException(player, maskFactoryCost);

        player.decreaseBudget(maskFactoryCost);

        //Add Facemasks to the player's containment techniques
        boolean alreadyExists = false;
        for (Containment cont:player.getContainTechniques()) {
            if (cont instanceof FaceMask){
                alreadyExists = true;
            }
        }

        if (!alreadyExists){
            FaceMask fmask = new FaceMask();
            player.addContainmentTech(fmask);
        }

        //increment the protection level
        for (Containment cont:player.getContainTechniques()) {
            if (cont instanceof FaceMask){
                player.incrementProtection_level(Constants.MASK_PROTECTION_Percentage,cont);
            }
        }

    }

    /**
     * upgrade quality of face masks
     * 1. Get the cost of upgrading mask quality
     * 2. if the player doesn't have enough budget, throw an exception
     *          otherwise:
     * 3. decrease budget according to the cost
     * 4. if exists, increment the protection level by Constants.UPGRADE_MASK_PROTECTION_Percentage
     * @param player
     * @param city
     * @throws NoEnoughBudgetException
     * @throws BudgetRunoutException
     */
    public void upgradeFMaskQuality(Player player, City city) throws NoEnoughBudgetException, BudgetRunoutException {
        //TODO
        // note: 1. you need to first check whether the current budget of the player is >= to the min budget allowed
        //          in order to sustain the player's city. Only when the current budget is bigger than that amount, you will continue,
        //          otherwise you will throw BudgetRunoutException.
        //          - To get the current budget of the player, you can use the getBudget() method of the player object
        //          - the min budget allowed (before upgrading the mask quality) is the constant MIN_ALLOWED_BUDGET, in the Constants class
        //       2. then you need to check if the budget of the player is enough for upgrading the mask (the cost is in
        //           Constants.UPGRADE_MASK_QUALITY_COST).
        //           - if the budget is not enough, throw NoEnoughBudgetException
        //           - Otherwise:
        //             - you subtract the upgrade mask cost from the budget of the player (player.decreaseBudget()),
        //             - *different from the labs*, you will need to check if the "FaceMask" class object exists in the containTechniques Arraylist of the player returned by "player.getContainTechniques()".
        //             if a FaceMask object exists, use it to increment protection level by calling incrementProtection_level() method of the player.
        //             The amount of protection level to increment is in Constants.UPGRADE_MASK_PROTECTION_Percentage.
        //             Remember to pass the "FaceMask" containment object to the player's incrementProtection_level() method as the second argument when you call it.
        //             If a FaceMask object does not exist in the containTechniques Arraylist of the player, then do nothing. DO NOT CREATE A new FaceMask object here!

        if (player.getBudget()<Constants.MIN_ALLOWED_BUDGET)
            throw new BudgetRunoutException(player);

        //update the player's budget if can afford the action
        int upgradeMaskQualityCost = Constants.UPGRADE_MASK_QUALITY_COST;
        boolean cantUpgradeMaskQuality = upgradeMaskQualityCost > player.getBudget();
        if (cantUpgradeMaskQuality) throw new NoEnoughBudgetException(player, upgradeMaskQualityCost);
        player.decreaseBudget(upgradeMaskQualityCost);

        //increment the protection level
        for (Containment cont:player.getContainTechniques()) {
            if (cont instanceof FaceMask){
                player.incrementProtection_level(Constants.UPGRADE_MASK_PROTECTION_Percentage,cont);
            }
        }
    }

    /**
     * Develop a Vaccine
     * 1. Get the cost of developing the vaccine
     * 2. if the player doesn't have enough budget, throw an exception
     *          otherwise:
     * 3. decrease budget according to upgrade cost
     * 4. if exists, increase the vaccination level by Constants.DEVELOP_VACCINE_Percentage
     * @param player
     * @param city
     * @throws NoEnoughBudgetException
     * @throws BudgetRunoutException
     */
    public void developVaccine(Player player, City city) throws NoEnoughBudgetException, BudgetRunoutException {
        //TODO
        // note: 1. you need to first check whether the current budget of the player is >= to the min budget allowed
        //          in order to sustain the player's city. Only when the current budget is bigger than that amount, you will continue,
        //          otherwise you will throw BudgetRunoutException.
        //          - To get the current budget of the player, you can use the getBudget() method of the player object
        //          - the min budget allowed (before developing Vaccine) is the constant MIN_ALLOWED_BUDGET, in the Constants class
        //       2. then you need to check if the budget of the player is enough for a developing vaccine  (the cost is in
        //           Constants.DEVELOP_VACCINE_COST).
        //           - if the budget is not enough, throw NoEnoughBudgetException
        //           - Otherwise:
        //             - you subtract the vaccine development cost from the budget of the player (player.decreaseBudget()),
        //             - *different from the labs*, you will need to check if the "Vaccination" class object that corresponding to the vaccination technology is already present.
        //             You can do that by checking the elements of the ArrayList of the player using "player.getContainTechniques()", and check to see if there is an element that is an instance of
        //             "Vaccination". If it does not exist create a new Vaccination object using the default constructor of "Vaccination" class, add this object to the player using "addContainmentTech()"
        //              method of the player. If a "Vaccination" object already exists (or you have just added it), use this object to increase the vaccination level of the player (as more people
        //              could be protected by vaccine now). To do that you need to use the player's incrementVaccination_level() method. Assume the vaccination level is incremented by the constant
        //              Constants.DEVELOP_VACCINE_Percentage. Remember to pass the "Vaccination" containment object to the player's incrementProtection_level() method as the second argument when you call it
        if (player.getBudget()<Constants.MIN_ALLOWED_BUDGET)
            throw new BudgetRunoutException(player);

        //update the player's budget if can afford the action
        int developVaccineCost = Constants.DEVELOP_VACCINE_COST;
        boolean cantDevelopVaccine = developVaccineCost > player.getBudget();
        if (cantDevelopVaccine) throw new NoEnoughBudgetException(player, developVaccineCost);
        player.decreaseBudget(developVaccineCost);

        //Add Vaccination to the player's containment techniques
        boolean alreadyExists = false;
        for (Containment cont:player.getContainTechniques()) {
            if (cont instanceof Vaccination){
                alreadyExists = true;
            }
        }

        if (!alreadyExists){
            Vaccination vacc = new Vaccination();
            player.addContainmentTech(vacc);
        }

        //update vaccination level
        for (Containment cont:player.getContainTechniques()) {
            if (cont instanceof Vaccination){
                player.incrementVaccination_level(Constants.DEVELOP_VACCINE_Percentage,cont);
            }
        }

    }

    /**
     * Upgrade a Vaccine
     * 1. Get the cost of upgrading the vaccine
     * 2. if the player doesn't have enough budget, throw an exception
     *          otherwise:
     * 3. decrease budget according to upgrade cost by Constants.UPGRADE_VACCINE_Percentage
     * @param player
     * @param city
     * @throws NoEnoughBudgetException
     * @throws BudgetRunoutException
     */
    public void upgradeVaccine(Player player, City city) throws NoEnoughBudgetException, BudgetRunoutException {
        //TODO
        // note: 1. you need to first check whether the current budget of the player is >= to the min budget allowed
        //          in order to sustain the player's city. Only when the current budget is bigger than that amount, you will continue,
        //          otherwise you will throw BudgetRunoutException.
        //          - To get the current budget of the player, you can use the getBudget() method of the player object
        //          - the min budget allowed (before upgrading the mask quality) is the constant MIN_ALLOWED_BUDGET, in the Constants class
        //       2. then you need to check if the budget of the player is enough for upgrading the mask (the cost is in
        //           Constants.UPGRADE_VACCINE_COST).
        //           - if the budget is not enough, throw NoEnoughBudgetException
        //           - Otherwise:
        //             - you subtract the upgrade mask cost from the budget of the player (player.decreaseBudget()),
        //             - *different from the labs*, you will need to check if a "Vaccination" class object exists in the containTechniques Arraylist of the player returned by "player.getContainTechniques()".
        //             if a Vaccination object exists, use it to increment vaccination level by incrementVaccination_level() method of the player.
        //             The amount of protection level to increment is in Constants.UPGRADE_VACCINE_Percentage.
        //             Remember to pass the "Vaccination" containment object to the player's incrementVaccination_level() method as the second argument when you call it.
        //             If a Vaccination object does not exist in the containTechniques Arraylist of the player, then do nothing. DO NOT CREATE A new Vaccination object here!
        if (player.getBudget()<Constants.MIN_ALLOWED_BUDGET)
            throw new BudgetRunoutException(player);

        //update the player's budget if can afford the action
        int upgradeVaccineCost = Constants.UPGRADE_VACCINE_COST;
        boolean cantUpgradeVaccine = upgradeVaccineCost > player.getBudget();
        if (cantUpgradeVaccine) throw new NoEnoughBudgetException(player, upgradeVaccineCost);
        player.decreaseBudget(upgradeVaccineCost);

        //increment the vaccination level
        for (Containment cont:player.getContainTechniques()) {
            if (cont instanceof Vaccination){
                player.incrementVaccination_level(Constants.UPGRADE_VACCINE_Percentage,cont);
            }
        }
    }

    /**
     * Ban Travel
     * 1. set city's travelBanned to true
     * 2. if doesn't, add Isolation to the player's containment techniques
     * 3. if exists, increment the protection level by Constants.TRAVELBAN_PROTECTION_Percentage
     * @param player
     * @param city
     */
    public void banTravel(Player player, City city) {
        //TODO
        // note: 1. we assume there is no cost for banning the travel, so you do not need to check the budget. It is also not required
        //          to check whether travel has been banned, you just need to always ban it. You can  use the setTravelBanned() method
        //          of the city object.
        //       2. *different from the labs*, you will need to check if the "Isolation" class object that corresponding to the Travel ban is already present.
        //          You can do that by checking the elements of the ArrayList of the player using "player.getContainTechniques()", and check to see if there is an element that is an instance of
        //          "Isolation". If it does not exist create a new isolation object using the default constructor of "isolation" class, add this object to the player using "addContainmentTech()"
        //          method of the player. If an "Isolation" object already exists (or you have just added it), use this object to increase the protection level of the player (as less people
        //          will be allowed to visit the city). To do that you need to use the player's incrementProtection_level() method. Assume the protection level is incremented by the constant
        //          Constants.TRAVELBAN_PROTECTION_Percentage. Remember to pass the "Isolation" containment object to the player's incrementProtection_level() method as the second argument when you call it

        //       2. then you increase protection level of the player by "Constants.TRAVELBAN_PROTECTION_Percentage" using the
        //               incrementProtection_level() of the player's "Containment" object. Mind that the "Containment" object is *private* in
        //               the player object.

        city.setTravelBanned(true);

        //add Isolation to the player's containment techniques
        boolean alreadyExists = false;
        for (Containment cont:player.getContainTechniques()) {
            if (cont instanceof Isolation){
                alreadyExists = true;
            }
        }

        if (!alreadyExists){
            Isolation iso = new Isolation();
            player.addContainmentTech(iso);
        }

        //increment the protection level
        for (Containment cont:player.getContainTechniques()) {
            if (cont instanceof Isolation){
                player.incrementProtection_level(Constants.TRAVELBAN_PROTECTION_Percentage, cont);
            }
        }
    }


    /**
     * Lift the travel ban
     * 1. set city's travelBanned to false
     * 2. if exists, remove Isolation from player's containment techniques
     * @param player
     * @param city
     */
    public void liftTravelBan(Player player, City city) {
        //TODO
        // note: 1. set travel ban to be false using the setTravelBanned() method of the city object, you can always do that directly without the need of doing any check
        //       2. check if an "Isolation" class object that corresponding to the Travel ban is present.
        //          You can do that by checking the elements of the ArrayList of the player using "player.getContainTechniques()", and check to see if there is an element that is an instance of
        //          "Isolation".
        //       3. if an "Isolation" object exists, remove it from the player's containTechniques ArrayList. You can do that by calling the removeContainmentTech() method of the player.
        //          Remember to pass the method with the "Isolation" object found.
        Isolation isoToBeRemoved=null;
        city.setTravelBanned(false);

        //remove Isolation from the player's containment techniques
        for (Containment cont:player.getContainTechniques()) {
            if (cont instanceof Isolation){
                isoToBeRemoved=(Isolation)cont;
            }
        }
        if (isoToBeRemoved!=null){player.removeContainmentTech(isoToBeRemoved);}

    }

    /**
     * Example string representation:
     * "HAStaff | READY, leadership, medicine, experience" - when isReady() == true
     * "HAStaff | DONE, leadership, medicine, experience" - when isReady() == false
     *
     * @return string representation of this object
     */
    @Override
    public String toString() {

        String toStr = String.format("HAStaff | %s, leadership %d, medicine %d, experience %d", isReady() ? "READY" : "DONE",leadership, medicine, experience);
        return toStr;
    }
}

