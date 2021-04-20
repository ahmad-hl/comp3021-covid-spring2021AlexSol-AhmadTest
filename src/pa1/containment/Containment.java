package pa1.containment;

/**
 * An abstract class that represents in-game technology
 */
public abstract class Containment {

    protected String name;
    protected int protection_level;
    protected int medication_level;
    protected int vaccination_level;

    /**
     * Initialize the levels to be 0
     */
    public Containment() {
        this.protection_level = 0;
        this.medication_level = 0;
        this.vaccination_level = 0;
    }

    //Trivial setters & getters
    public void setProtection_level(int protection_level) {
        this.protection_level = protection_level;
    }

    public void setMedication_level(int medication_level) {
        this.medication_level = medication_level;
    }

    public void setVaccination_level(int vaccination_level) {
        this.vaccination_level = vaccination_level;
    }

    public int getProtection_level() {
        return protection_level;
    }

    public int getMedication_level() {
        return medication_level;
    }

    public int getVaccination_level() {
        return vaccination_level;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "name=" + name +", protection level=" + protection_level
                +", medication level=" + medication_level +", vaccination level=" + vaccination_level +"\n";
    }
}
