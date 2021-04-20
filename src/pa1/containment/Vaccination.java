package pa1.containment;


public class Vaccination extends Containment {

    public Vaccination() {
        this.name = "vaccination";
    }

    @Override
    public int getVaccination_level() {
        return vaccination_level;
    }

}
