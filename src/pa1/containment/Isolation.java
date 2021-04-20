package pa1.containment;

public class Isolation extends Containment {

    public Isolation() {
        super();
        this.name = "isolation";
    }

    @Override
    public int getProtection_level() {
        return protection_level;
    }
}
