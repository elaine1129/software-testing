package st.assignment;

import st.assignment.lib.Printable;

import java.util.Objects;

public class DeliveryRates implements Printable {
    private String area;

    public String getArea() {
		return area;
	}
    private double rates;

    public double getRates() {
		return rates;
	}

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DeliveryRates))
            return false;

        DeliveryRates dr = (DeliveryRates) obj;

        return Objects.equals(dr.getArea(), this.getArea()) && Objects.equals(dr.getRates(), this.getRates());
    }

    @Override
    public String toString() {
        return "DeliveryRates{" +
                "area='" + area + '\'' +
                ", rates=" + rates +
                "} \n";
    }

    public DeliveryRates(String area, double rates){
        this.area = area;
        this.rates = rates;
    }

    @Override
    public String toTable() {
        return String.format("%s,%.2f", area, rates);
    }
}