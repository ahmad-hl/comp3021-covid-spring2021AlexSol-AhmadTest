package pa1.containment;

public class Treatment extends Containment {

    public Treatment() {
        super();
        this.name = "treatment";
    }

    @Override
    public int getMedication_level() {
        return medication_level;
    }
}
