package st.assignment;

import st.assignment.lib.Printable;
import st.assignment.lib.Writalbe;

import java.util.Objects;

public class Payment implements Writalbe, Printable {

	
	private String paymentMethod;
	private boolean paymentStatus;
	private double totalPrice;
	private Order paymentOrder;

	public Payment setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
		return this;
	}

	public boolean getPaymentStatus() {
		return paymentStatus;
	}

	public Payment setPaymentStatus(boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
		return this;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public Payment setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
		return this;
	}

	public Order getPaymentOrder() {
		return paymentOrder;
	}

	public Payment setPaymentOrder(Order paymentOrder) {
		this.paymentOrder = paymentOrder;
		return this;
	}

	public Payment() {
	}
	
	public String getPaymentMethod() {
		return paymentMethod;
	}

	public Payment(String paymentMethod, boolean paymentStatus, double totalPrice, Order paymentOrder) {
		this.paymentMethod = paymentMethod;
		this.paymentStatus = paymentStatus;
		this.totalPrice = totalPrice;
		this.paymentOrder = paymentOrder;
	}

	public Payment(String paymentMethod){
		this.paymentMethod = paymentMethod;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Payment))
			return false;

		Payment pay = (Payment) obj;

		return Objects.equals(pay.getPaymentMethod(), paymentMethod);
	}


	@Override
	public String toTable() {
		return null;
	}


	@Override
	public String toFile() {
		return String.format("%s,%s,%.2f,%s", paymentOrder.getOrderID(), paymentMethod, totalPrice, paymentStatus);
	}
}
