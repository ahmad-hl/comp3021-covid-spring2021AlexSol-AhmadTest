package pa1;

import pa1.containment.*;
import pa1.exceptions.MedicalException;
import pa1.HAstaff.HealthAuthorityStaff;
import pa1.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A class that represents a player in the game.
 * It contains the player's attribute such as budget and points,
 * as well as the player's assets such as ministers, cities, and virus containment techniques.
 */
public class Player {

    // Assets
    private final List<HealthAuthorityStaff> haStaffs = new ArrayList<>();
    private final List<Containment> containTechniques = new ArrayList<>();

    // Attributes
    private final String name;
    private int budget;
    private int tourismIncome;
    private int points;

    private City city;

    /**
     * Initializes member variables.
     * @param name
     * @param budget
     * @param tourismIncome
     * @param points
     */
    public Player(String name, int budget, int tourismIncome, int points) {
        this.name = name;
        this.budget = budget;
        this.tourismIncome = tourismIncome;
        this.points = points;
    }

    @Override
    public String toString() {
        String toStr = String.format("Player: %s | budget: %d | tourism income: %d | points: %d\n",
                name, budget, tourismIncome, points);
        toStr += city.toString() +"\n";

        String contNames = "";
        int protection_level = 0;
        int vaccination_level = 0;
        int medication_level = 0;
        for (Containment cont:containTechniques) {
                contNames += cont.getName() +",";
            if (cont instanceof FaceMask || cont instanceof Isolation) {
                protection_level += cont.getProtection_level();
            }else if (cont instanceof Vaccination) {
                vaccination_level = cont.getVaccination_level();
            } else if (cont instanceof Treatment) {
                medication_level = cont.getMedication_level();
            }
        }
        toStr += String.format("Health Authority Staff:\n");
        for (HealthAuthorityStaff healthAuthorityStaff :  getHAStaffs()) {
            toStr += String.format("\t %s \n", healthAuthorityStaff);
        }

        toStr += String.format("Containment : %s | protection level: %d | vaccination level: %d | medication level: %d ",
                contNames, protection_level, vaccination_level, medication_level);

        return toStr;
    }

    /**
     * Decreases the player's budget by amount
     * Cap the value to 0.
     *
     * @param decrement
     */
    public void decreaseBudget(int decrement) {

        budget = Math.max(0, budget - decrement);
    }

    /**
     * Increase the player's budget by amount
     *
     * @param increment
     */
    public void increaseBudget(int increment) {
        //TODO
        // if increment>0, then increase tbe budget by increment
        if (increment > 0)
            budget += increment;
    }



    /**
     * Adds player's points by specified increment.
     *
     * @param increment
     */
    public void addPoints(int increment) {

        if (increment > 0)
           points += increment;
    }

    /**
     * Decreases the player's points by specified decrement.
     *
     * @param decrement
     */
    public void decreasePoints(int decrement) {

        points = Math.max(0, points - decrement);
    }

    /**
     * @return true if the player has at least one ready HAStaff
     */
    public boolean hasReadyHAStaff() {
        //TODO
        // for all the staffs in the ArrayList "haStaffs", check if that isReady() will return true for at
        // least one of them. If this is the case return true, otherwise return false.
        for (HealthAuthorityStaff haStaff: getHAStaffs()) {
            if(haStaff.isReady())
                return true;
        }
        return false;
    }

    /**
     * Compute new infected cases and updated total infected cases
     *
     * 1. get current protection and vaccination level
     * 2. compute: IF = .5*(100-protection level) + .5*(100-vaccination level)
     * 3. define a factor called spreadRate  = 1
     * 4.  if there is no protection at all:
     *      set spreadRate to 3, i.e., on average an active patient can pass the virus to 3 more people)
     * 5. compute: new infected cases = spreadRate * IF * infectedCases * population
     * 6. add new cases to city's total infected cases
     * @throws MedicalException
     */
    public void computeNewInfectedCases() throws MedicalException {
        // do not change the two lines below!
        int currProtectionLevel = 0;
        int currVaccinationLevel = 0;

        //TODO
        // This part is different from Lab6/Lab7. Do not directly copy and paste from the two labs!
        // note: 1. you will need to check through the ArrayList "containTechniques" of the player, and see if there
        //          is an "Isolation" object (using instanceof), if there is, increment the currProtectionLevel by "protection_level"
        //          amount of the "Isolation" object. Then you also need to check in the ArrayList containTechniques of the player,
        //          whether there is the "FaceMask" object, if there is the "FaceMask: object, further increment
        //          currProtectionLevel by "protection_level" amount of the "FaceMask" object.
        //       2. you also need to check the ArrayList "containTechniques" of the player, and see if there is a "Vaccination"
        //          object. If there is, increment currVaccinationLevel by the amount of vaccination_level in the "Vaccination" object

        for (Containment contTech: containTechniques) {
            if (contTech instanceof Isolation || contTech instanceof FaceMask)
                currProtectionLevel += contTech.getProtection_level();
            else if (contTech instanceof Vaccination)
                currVaccinationLevel = contTech.getVaccination_level();
        }

        //TODO above

        // don not change the lines below!
        double increaseFactor = 0.5 * (Constants.MAX_LEVEL - currProtectionLevel)/Constants.MAX_LEVEL + 0.5 * (Constants.MAX_LEVEL - currVaccinationLevel)/Constants.MAX_LEVEL;
        //If there is no protection measures were applied, set spread rate to 3
        double spreadRate= 3.0*increaseFactor;

        int newInfectedCases =(int) Math.ceil(spreadRate * increaseFactor * city.getActiveCases() );

        // memorize the new cases of the previous round, so that we can calculate the difference in new cases for 2 consecutive rounds
        // this is not present in the labs.
        city.setNumFormerCases(city.getNumNewCases());


        city.setNumNewCases(newInfectedCases);

        // the following method will throw MedicalException if the total active cases is the same as population of the city
        city.increaseActiveCases(newInfectedCases);
    }

    /**
     * Unpredicted disasters at the end of turn.
     * There are three types of disasters, that affect both protection level, vaccination_level, and medical level.
     * A disaster happens when disasterOccured <= 0.4, it halves the level.
     * Otherwise the level is left unchanged
     * disasterType [0: Fake face masks, 1: drop vaccination efficiency, 3: destruction of medication facility]
     *
     * 1. pick two random variables, one to select disasterType, and another to select disasterOccured propoability
     * 2. if there is disasterOccured:
     *      2.1. switch case 0: halve the protection level
     *      2.2.             1: halve the vaccination level
     *      2.3.             2. decrease the medication facility
     */
    public void generateUnexpectedDistasters() {

        // Do not change the lines below!
        Random rand = new Random();
        int disasterType = rand.nextInt(3);
        boolean disasterOccured = rand.nextDouble() <= 0.4;

        //TODO
        // note: 1. you must use the variables/objects,  rand, disasterType, disasterOccured defined above
        //       2. if disasterOccured is true, then:
        //          - if disasterType is 0, there are fake masks in the market, you will need to check if the ArrayList
        //            "containTechniques" of the player contains a "FaceMask" object. If there is such an object, run
        //             halfProtection_level() and then output the message "\nDisaster: Fake face masks that halves the protection\n",
        //             and then returns
        //          - if disasterType is 1, then weather and changes in the virus has halved the vaccination efficiency,
        //             you will need to check if the ArrayList "containTechniques" of the player contains a "Vaccination" object.
        //             If there is such an object, run halfVaccination_level() method, and then output the message
        //             "\nDisaster: Weather/physical changes that halves the vaccination efficiency\n", and then returns
        //          - if disasterType is 2, then one hospital goes out of service *permanently* you will need to
        //             run the decreaseMedicationFacility() of the city object of the player, and then returns


        if (disasterOccured){
            switch (disasterType) {
                case 0:
                    for (Containment cont:containTechniques) {
                        if (cont instanceof FaceMask) {
                            halfProtection_level();
                            System.out.println("\nDisaster: Fake face masks that halves the protection\n");
                            break;
                        }
                    }
                    break;
                case 1:
                    for (Containment cont:containTechniques) {
                        if (cont instanceof Vaccination) {
                            halfVaccination_level();
                            System.out.println("\nDisaster: Weather/physical changes that halves the vaccination efficiency\n");
                            break;
                        }
                    }
                    break;
                case 2:
                    city.decreaseMedicationFacility();
                    System.out.println("\nDisaster: One medication facility is out of service\n");
                    break;
            }
        }
    }


    /**
     * Adds a containment technique.
     *
     * @param cont
     */
    public void addContainmentTech(Containment cont) {
        //TODO add the containment object "cont" to the ArrayList "containTechniques" of the player
        // note: you need to call the add() method of the ArrayList object
        containTechniques.add(cont);
    }

    /**
     * Removes a containment technique.
     *
     * @param cont
     */
    public void removeContainmentTech(Containment cont) {
        //TODO remove the containment object "cont" to the ArrayList "containTechniques" of the player
        // note: you need to call the remove() method of the ArrayList object
        containTechniques.remove(cont);
    }

    public void setCity(City city) {
        this.city = city;
    }

    /**
     * Increment protection level.
     * 1. if cont in the containment techniques, increase the protection level by inLevel and set upperbound to 100
     *
     * @param inLevel
     * @param cont
     */
    public void incrementProtection_level(int inLevel, Containment cont ) {
        //TODO
        // note: if the player's ArrayList "containTechniques", contains the "Containment" object "cont" passed,
        //        increment the protection level of the Containment object "cont" by the amount
        //        of "intLevel" passed. But, you need to make sure the protection level of the "cont" object will never
        //        exceed 100 (i.e. 100 is the maximum). You may find contains() method of the ArrayList useful, you may also find
        //        indexOf() and get() methods of the ArrayList useful. The documentation of ArrayList is at https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/util/ArrayList.html
        if (containTechniques.contains(cont)) {
            int index = containTechniques.indexOf(cont);
            int newProtectionLevel =  Math.min(100, cont.getProtection_level() + inLevel);
            containTechniques.get(index).setProtection_level(newProtectionLevel);
        }
    }

    /**
     * halve the protection level.
     * 1. if FaceMask is in the containment techniques, halve the protection level
     *
     */
    public void halfProtection_level() {
        //TODO
        // note: you checks to see if there is "FaceMask" object in the "containTechniques" ArrayList of the player.
        //       if this is the case, you set the protection level of the object to 50% of its original value.
        //       This function is called in the generateUnexpectedDistasters() method, when there is a disaster and the disaster type is 0
        for (Containment cont:containTechniques) {
            if (cont instanceof FaceMask) {
                int newValue = (int) Math.ceil(cont.getProtection_level() * 0.5f);
                cont.setProtection_level(newValue);
            }
        }
    }

    /**
     * Increment vaccination level.
     * 1. if cont in the containment techniques, increase the vaccination level by inLevel  and set upperbound to 100
     * @param inLevel
     * @param cont
     */
    public void incrementVaccination_level(int inLevel, Containment cont) {
        //TODO
        // note: if the player's ArrayList "containTechniques", contains the "Vaccination" object "cont" passed,
        //        increment the vaccination_level of the "Vaccination" object "cont" by the amount
        //        of "intLevel" passed. But, you need to make sure the protection level of the "cont" object will never
        //        exceed 100 (i.e. 100 is the maximum). You may find contains() method of the ArrayList useful, you may also find
        //        indexOf() and get() methods of the ArrayList useful. The documentation of ArrayList is at https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/util/ArrayList.html

        if (containTechniques.contains(cont)) {
            int index = containTechniques.indexOf(cont);
            int newVaccLevel =  Math.min(100, cont.getVaccination_level() + inLevel);
            containTechniques.get(index).setVaccination_level(newVaccLevel);
        }
    }

    /**
     * halve the vaccination level.
     * 1. if Vaccination is in the containment techniques, halve the vaccination level
     *
     */
    public void halfVaccination_level() {
        //TODO
        // note: you checks to see if there is "Vaccination" object in the "containTechniques" ArrayList of the player.
        //       if this is the case, you set the Vaccination level of the object to be 50% of its original value.
        //       This function is called in the generateUnexpectedDistasters() method, when there is a disaster and the disaster type is 1

        for (Containment cont:containTechniques) {
            if (cont instanceof Vaccination) {
                int newValue = (int) Math.ceil(cont.getVaccination_level() * 0.5f);
                cont.setVaccination_level(newValue);
            }
        }
    }

    // Trivial getters
    public List<HealthAuthorityStaff> getHAStaffs() {
        return haStaffs;
    }

    public City getCity() {
        return city;
    }

    public List<Containment> getContainTechniques() {
        return containTechniques;
    }

    public String getName() {
        return name;
    }

    public int getBudget() {
        return budget;
    }

    public int getTourismIncome() {
        return tourismIncome;
    }

    public int getPoints() {
        return points;
    }

}
