package st.assignment;

import st.assignment.lib.Printable;
import st.assignment.lib.Writalbe;

import java.util.List;
import java.util.Objects;

public class Order implements Writalbe, Printable
{
    private User orderUser;
    
    private List <OrderDetails> itemlist;
    private double totalPrice;
    private String orderID;
	private String orderStatus;
    private Payment orderPayment;

	public Order setOrderUser(User orderUser) {
		this.orderUser = orderUser;
		return this;
	}

	public Order setItemlist(List<OrderDetails> itemlist) {
		this.itemlist = itemlist;
		return this;
	}

	public Order setOrderID(String orderID) {
		this.orderID = orderID;
		return this;
	}

	public Order setOrderPayment(Payment orderPayment) {
		this.orderPayment = orderPayment;
		return this;
	}

	public Order setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
		return this;
	}

	public List<OrderDetails> getItemList()
	{
		return itemlist;
	}

	public Order setPayment(Payment payment){
		orderPayment = payment;
		return this;
	}

    public Address getDeliveryAddress() {

    	return orderUser.getUserAddress();
    }

	public User getOrderUser() {
		return orderUser;
	}

	public List<OrderDetails> getItemlist() {
		return itemlist;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public Order setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
		return this;
	}

	public String getOrderID() {
		return orderID;
	}

	public Order(User orderUser, List <OrderDetails> itemlist, String orderID) {
		this.orderUser = orderUser;
		this.itemlist = itemlist;
		this.orderID = orderID;
	}

	//for read from file
	public Order(User orderUser, String orderID, double totalPrice, String orderStatus, List  <OrderDetails> itemlist, Payment paymentMethod) {
		this.orderUser = orderUser;
		this.orderID = orderID;
		this.totalPrice = totalPrice;
		this.orderStatus = orderStatus;
		this.itemlist = itemlist;
		this.orderPayment = paymentMethod;
	}
	public Order(){}

	@Override
	public String toFile() {
		String orderList = String.format("%s,%s,%.2f,%s,%s", orderID, orderUser.getMemberID(),totalPrice, orderStatus, orderPayment.getPaymentMethod());

		return orderList;
	}

	@Override
	public String toTable() {
		return String.format("%s,%.2f,%s,%s", orderID, totalPrice,orderStatus, orderPayment.getPaymentMethod());
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Order))
			return false;

		Order od = (Order) obj;

		return Objects.equals(od.getOrderID(), orderID);
	}

}