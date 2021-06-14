package st.assignment;

import st.assignment.lib.Printable;
import st.assignment.lib.Writalbe;

public class Member extends User  {
    private String memberID;
    private String phoneNumber;
    private String password;

	public Member setMemberID(String memberID) {
		this.memberID = memberID;
		return this;
	}

	public Member setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public Member setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getPassword(){
		return password;
	}
    
	public String getMemberID() {
		return memberID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Member(){}

	public Member(String memberID, String name, String password, String phoneNumber, Address userAddress) {
		super(name, userAddress);
		this.phoneNumber = phoneNumber;
		this.memberID = memberID;
		this.password = password;
	}

	@Override
	public String toFile() {
		return String.format("%s,%s,%s,%s,%s",
				getMemberID(), super.getName(), getPassword(), getPhoneNumber(), super.getUserAddress().toFile());
	}

	@Override
	public String toTable() {
		return toFile();
	}
}