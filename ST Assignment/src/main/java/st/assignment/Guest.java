package st.assignment;

public class Guest extends User{
    private String GuestID = "GUEST";

    public String getMemberID() {
        return GuestID;
    }

    public Guest(String name, Address address) {
        super(name, address);
    }
    public Guest(){}

    @Override
    public String toTable() {
        return String.format("%s", getMemberID());
    }

    @Override
    public String toFile() {
        return toTable();
    }
}