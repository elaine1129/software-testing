package st.assignment;

import st.assignment.lib.Printable;
import st.assignment.lib.Writalbe;

import java.util.Objects;

public abstract class User implements Writalbe, Printable {
    private String name;
    private Address userAddress;
    
    public abstract String getMemberID();
    
    public String getName() {
		return name;
	}

	public Address getUserAddress() {
		return userAddress;
	}

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof User))
            return false;

        User user = (User) obj;

        return Objects.equals(user.getMemberID(),this.getMemberID());
    }

	public User(String name, Address userAddress) {
        this.name = name;
        this.userAddress = userAddress;
    }

    public User(){}
}