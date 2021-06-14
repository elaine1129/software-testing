package st.assignment;

import st.assignment.lib.Printable;
import st.assignment.lib.Writalbe;

import java.util.Objects;

public class OrderDetails implements Writalbe, Printable

{
    private String OrderID;
	private String OrderDetailsID;

	public String getOrderDetailsID() {
		return OrderDetailsID;
	}

	private Item item;
    private int quantity;

    public String getOrderID() {
        return OrderID;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }


	private double itemPrice;


    //for calculate order total
    public OrderDetails(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    //for read from file
    public OrderDetails(String orderID, String orderDetailsID, Item item, int quantity, double itemPrice) {
        OrderID = orderID;
        OrderDetailsID = orderDetailsID;
        this.item = item;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public OrderDetails(String orderDetailsID, Item item, int quantity, double itemPrice)
	{
		OrderDetailsID = orderDetailsID;
		this.item = item;
		this.quantity = quantity;
		this.itemPrice = itemPrice;
	}

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof OrderDetails))
            return false;

        OrderDetails od = (OrderDetails) obj;

        return Objects.equals(od.getOrderID(), OrderID) && Objects.equals(od.getOrderDetailsID(), OrderDetailsID);
    }



    @Override
	public String toFile() {
		return String.format("%s,%s,%s,%.2f",OrderDetailsID, item.getItemName(), quantity, itemPrice);
	}

	@Override
	public String toTable() {
		return String.format("%s,%s,%.2f", item.getItemName(), quantity, itemPrice);
	}
}
