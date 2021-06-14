package st.assignment;

import st.assignment.lib.Printable;
import st.assignment.lib.Writalbe;

import java.util.Objects;

public class Item implements Writalbe, Printable {
	private int itemId;
	private String itemName;
	private String itemType;
	private double memberPrice;
	private double nonMemberPrice;
	private boolean promotion;
	public String getItemName() {
		return itemName;
	}

	public int getItemId() {
		return itemId;
	}

	public double getMemberPrice() {
		return memberPrice;
	}

	public double getNonMemberPrice() {
		return nonMemberPrice;
	}

	public boolean isPromotion() {
		return promotion;
	}

	public String toString(){
		return String.format("%d, %s %s %.2f %.2f %s", itemId, itemName, itemType, memberPrice, nonMemberPrice, isPromotion());
	}

	// For calculate test
	public Item(double memberPrice, double nonMemberPrice, boolean promotion) {
		this.memberPrice = memberPrice;
		this.nonMemberPrice = nonMemberPrice;
		this.promotion = promotion;
	}

	public Item(int itemId, String itemName, String itemType, double memberPrice, double nonMemberPrice, boolean promotion) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemType = itemType;
		this.memberPrice = memberPrice;
		this.nonMemberPrice = nonMemberPrice;
		this.promotion = promotion;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Item))
			return false;

		Item item = (Item) obj;

		return Objects.equals(item.getItemName(), itemName);
	}


	@Override
	public String toFile() {
		return String.format("%d,%s,%s,%.2f,%.2f,%s", itemId, itemName, itemType, memberPrice, nonMemberPrice, isPromotion()?"Yes":"No");
	}

	@Override
	public String toTable() {
		return toFile();
	}
}
