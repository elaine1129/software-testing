package st.assignment;

import st.assignment.lib.Writalbe;

import java.nio.file.Watchable;
import java.util.Objects;

public class Address implements Writalbe
{
    private String area;
    private String district;
    private String postalCode;
    private String state;
    private String streetName;
    private String unitNumber;
    
    
	public String getArea() {
		return area;
	}

	public String getDistrict() {
		return district;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getState() {
		return state;
	}

	public String getStreetName() {
		return streetName;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public Address(){

	}

	public Address(String unitNumber, String streetName, String postalCode, String area, String district, String state 
			) {
		super();
		this.area = area;
		this.district = district;
		this.postalCode = postalCode;
		this.state = state;
		this.streetName = streetName;
		this.unitNumber = unitNumber;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Address))
				return false;

		Address add = (Address) obj;

		return Objects.equals(unitNumber, add.getUnitNumber()) && Objects.equals(streetName, add.getStreetName()) &&
				Objects.equals(postalCode, add.getPostalCode()) && Objects.equals(area, add.getArea()) &&
				Objects.equals(district, add.getDistrict()) && Objects.equals(state, add.getState());

	}

	@Override
	public String toFile() {
		return String.format("%s,%s,%s,%s,%s,%s", getUnitNumber(),getStreetName(),getPostalCode(),getArea(),getDistrict(),getState());
	}
}