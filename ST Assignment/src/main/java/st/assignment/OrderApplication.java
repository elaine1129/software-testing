package st.assignment;

import st.assignment.lib.TablePrinter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderApplication {

    private MyScanner in;
    private ApplicationData appData;
    private TablePrinter tablePrinter = new TablePrinter();

    public OrderApplication(){

	}

	public OrderApplication setApplicationData(ApplicationData applicationData){
    	this.appData = applicationData;
    	return this;
	}

	public OrderApplication setTablePrinter(TablePrinter tablePrinter) {
		this.tablePrinter = tablePrinter;
		return this;
	}

	public OrderApplication(MyScanner myScanner, ApplicationData appData){
    	this.in = myScanner;
    	this.appData = appData;
	}


    public static void main(String[] args) throws IOException{
		MyScanner in = new MyScanner();
		ApplicationData appData = new ApplicationData();
		appData.readDataFromFile();
		OrderApplication app = new OrderApplication(in, appData);
		app.menu();
    }

    public void menu(){
		while (true){
			while (!login()){};
			mainMenu();
		}
	}

    public void mainMenu(){
		int choice = 0;
		do{
			System.out.println();
			System.out.println("Welcome, "+ appData.getLoggedInUser().getName());
			System.out.println("1- View item menu");
			System.out.println("2- Place order");
			System.out.println("3- Check order status");
			System.out.println("4- Logout");
			System.out.print("Enter option: ");
			choice = in.nextInt();

			switch (choice){
				default:{
					System.out.println("Please enter between the range!");
					break;
				}
				case 1:{
					view_item_menu(appData.getItemArray());
					break;
				}
				case 2:{
					place_order(appData.getItemArray(),appData.getLoggedInUser());
					break;
				}
				case 3:{
					check_order_status(appData.getLoggedInUser());
					break;
				}
				case 4:{
					//record order to file
					appData.updateOrderToFile();
//					login();
				}
			}
		}while(choice!=4);
	}

    public void line(){
		System.out.println("-----------------------------");
	}

	public boolean login(){
		int choice;
		String memberName;
		String password;
		boolean logged = false;
        appData.setLoggedInUser(null);

			System.out.println();
			System.out.println("Kiah's Cake Ordering Application (Only within Melaka area)");
			line();
			System.out.println("1. Login ");
			System.out.println("2. Login As Guest");
			System.out.println("3. Sign up member");
			System.out.println("4. Quit");
			System.out.print("Enter option: ");
			while (true){
				try{
					choice = in.nextInt();
					break;
				}catch (IllegalArgumentException e){
					System.out.print(e.getMessage());
				}
			}
			switch (choice){
				case 1:{
					System.out.print("Enter name: ");
					memberName = in.next();

					System.out.print("Enter password: ");
					password = in.next();

					for (User user : appData.getUserArr()) {
						if (user instanceof Member) {
							if (memberName.equals(((Member) user).getName())) {
								if (password.equals(((Member) user).getPassword())) {
									appData.setLoggedInUser(user);
									System.out.println("Logged in.");
									logged = true;
									break;
								}
							}
						}
					}
					if(!logged)
						System.out.println("ID or password incorrect, please try again");
					break;
				}
				case 2:{
					System.out.print("Please enter your name: ");
					String name = in.nextLine();

           		 	Address tempAd = get_address_info();

					appData.setLoggedInUser(new Guest (name, tempAd));
					System.out.println("Logged in as Guest");
					logged = true;
					break;
				}
				case 3:{
					System.out.print("Enter name: ");
					String name = in.nextLine();

					System.out.print("Enter password: ");
					String pass = in.nextLine();
					System.out.print("Enter Phone Number: ");
					String phonenum = in.nextLine();

					Address tempAd = get_address_info();

					Member newMember = new Member (appData.newMemberID(), name, pass, phonenum, tempAd);
					//write to file
					appData.addNewMember(newMember);
					System.out.println("Registration successful.");
					break;
					//back to login menu
				}
				case 4:{
					System.exit(0);
				}
				default:{
					System.out.println("Please enter between 1 - 4");
					break;
				}
			}

		return logged;
	}

	public Address get_address_info()
	{
		System.out.print("Enter unit No: ");
		String unitno = in.nextLine();
		System.out.print("Enter street name: ");
		String street = in.nextLine();
		System.out.print("Enter postal code: ");
		String postalcode = in.nextLine();

		boolean validArea = false;
		String area = "";
		do{
			System.out.print("Enter area (Only area in Melaka): ");
			area = in.nextLine();
			validArea = Arrays.stream(appData.getDeliveryRatesArr()).map(DeliveryRates::getArea).anyMatch(area::equals);
			if(!validArea){
				System.out.println("Not an area within melaka! Please try again. ");
			}
		}while (!validArea);

		System.out.print("Enter district: ");
		String district = in.nextLine();
		System.out.println("State : Melaka");
		String state = "Melaka";

		Address tempAd = new Address(unitno, street, postalcode, area, district, state);

		return tempAd;
	}


	//FOR calculate price ----
	private double calculate_member_price(Item item){
    	if(item.isPromotion()){
			return item.getMemberPrice()*0.95;
		}
		return item.getMemberPrice();
	}
	private double calculate_guest_price(Item item){
		if(item.isPromotion()){
			return item.getNonMemberPrice()*0.95;
		}
		return item.getNonMemberPrice();
	}
	private double calculate_single_item_price(Item item, User user){
		if (user instanceof Member){
			return calculate_member_price(item);
		}else
			return calculate_guest_price(item);
	}
	//FOR calculate price ---- END

    public double calculate_order_total (Order order, User user)
    {
		List <OrderDetails> itemlist = order.getItemList();
	    double total = 0.0;

		for (int i = 0; i < itemlist.size(); i++) {
			double price;
			price = calculate_single_item_price(itemlist.get(i).getItem(), user);
			total += price*itemlist.get(i).getQuantity();
		}
	    return total;
    }

     public double get_delivery_rate(User user)
	{
	    double deliveryrates =0.0;
	    boolean found = false;


	    for (int i = 0; i< appData.getDeliveryRates().size();i++)
	    {
	    	if (user.getUserAddress().getArea().equals(appData.getDeliveryRates().get(i).getArea()))
	    	{
	    		deliveryrates = appData.getDeliveryRates().get(i).getRates();
	    		found = true;
	    		break;
	    	}
	    }

	    return deliveryrates;
    }


    public void view_item_menu(List <Item> itemArray)
    {
		//int pagesize = 10;
		//int totalpage = itemArray.size() / pagesize;
		//int choice = 0;

    	//txt file to array
		String[] header = {"Id","Name","Type","Member Price (RM)","Non-Member Price (RM)", "Promotional Item"};

		tablePrinter.printTable(itemArray, header);
    }


    public void place_order(List<Item> itemArray,User user)
    {
    		int oItemId=0;
    		Item tempItem;
        	int oItemQuantity=0;
        	String oDetailsID;
        	char repeat=0;
        	double total = 0.0;
        	List <OrderDetails> orderItemList= new ArrayList <OrderDetails>();
        	Order order;

			//view item menu
			view_item_menu(itemArray);
			oDetailsID = "";
			int counter = 1;

			String orderID= String.format("O%04d",appData.getOrderArray().size());
			//loop (prompt enter item name & quantity)
			do
			{
				System.out.print("Enter item id: ");

				while (true){
					try{
						oItemId = in.nextItemID();
						break;
					}catch (IllegalArgumentException e){
						System.out.print(e.getMessage());
					}
				}

				tempItem = appData.getItemById(oItemId);

				System.out.print("Enter quantity: ");
				while (true){
					try{
						oItemQuantity = in.nextQuantity();
						break;
					}catch (IllegalArgumentException e){
						System.out.print(e.getMessage());
					}
				}

				double oTotalSinglePrice = oItemQuantity * calculate_single_item_price(tempItem, user);

				oDetailsID = String.format("OD%04d", counter);
				orderItemList.add(new OrderDetails(oDetailsID, tempItem, oItemQuantity, oTotalSinglePrice));
				counter++;

				while (true){
					System.out.print("Order more items (y-yes/n-no)?: ");
					repeat = in.next().charAt(0);

					if(!(repeat=='y' || repeat == 'Y' || repeat == 'n' || repeat == 'N')){
						System.out.println("Invalid option. Please enter again.");
					}else {
						break;
					}
				}

			}while(repeat=='y' || repeat == 'Y');

		order = new Order (user, orderItemList, orderID);

		//print orderdetails
		System.out.println("Items ordered are listed below: ");
		String[] header = {"Item Name", "Quantity", "Price"};
		tablePrinter.printTable(order.getItemList(), header);

		//convert to 2 dp
		Double ordertotal = Double.parseDouble(String.format("%.02f", calculate_order_total(order, user)));

		System.out.println("Order Total: "  + ordertotal);

		// if total order is less than 0
		if(ordertotal <= 0){
			throw new IllegalArgumentException("Total order is less than 0");
		}

		// if order < RM 25 additional charge
		if (ordertotal<25)
		{
			ordertotal +=3;
		}

		Double deliveryrates = get_delivery_rate(user);

		System.out.print("Delivery Rate: " + deliveryrates + "\n");
		total = ordertotal + deliveryrates;
		System.out.print("Total Payment: " + total + "\n");
		order.setTotalPrice(total);
		//make payment
		order.setPayment(make_payment(order));

		//add new order
		appData.addOrder(order);
    }



	public Payment make_payment(Order order)
	{
		double totalPrice = order.getTotalPrice();
		System.out.println("Total amount to pay is RM " + totalPrice );

		Boolean paymentStatus = false;
		String orderStatus="Pending For Payment";
		String paymentMethod ="";


		//need to add in assumption
		do{
			System.out.print("Enter payment method (A-Online Banking/B-Credit Card): ");
			paymentMethod = in.nextLine();

			if (paymentMethod.equalsIgnoreCase("a"))
			{
				paymentMethod = "Online Banking";
				//online banking gateway
				paymentStatus = true;

			}else if(paymentMethod.equalsIgnoreCase("b"))
			{
				paymentMethod = "Credit Card";
				// card payment gateway
				paymentStatus = true;
			}else
			{
				System.out.println("Payment can be done by online banking or credit card only.");
			}
		}while(!paymentStatus);


		if(paymentStatus == true)
		{
			orderStatus = "Paid & Ready for Delivery";
			System.out.println("Payment Success!");
		}

		Payment payment = new Payment(paymentMethod, paymentStatus, totalPrice, order);
		order.setOrderStatus(orderStatus);
		order.setPayment(payment);
		return payment;
	}


public void check_order_status(User orderUser)
{
	//display all orders from order array (show orderid and orderstatus)
	System.out.println();
	List <Order> orderlist = appData.getOrderArray();
	List <Order> userorder = new ArrayList<Order>();

	for (int i = 0; i< orderlist.size();i++)
	{
		if (orderlist.get(i).getOrderUser() == orderUser)
		{
			userorder.add(orderlist.get(i));
		}
	}

	//back to main menu if no order made
	if (userorder.size() < 1){
		System.out.println("No order made.");
		System.out.println("Press enter to continue...");
		in.nextLine();
		return;
	}


	String header[] = {"Order ID","Total Price", "Status", "Payment Method"};
	tablePrinter.printTable(userorder, header);


	//select order num
	//print order list
	//select orderdetails
	Order selectedOrder = null;
	//boolean reenter = false;
	while (true){
		System.out.print("Enter order (enter 0 to back): ");

		String orderId = in.nextLine();

		//enter 0 to back main menu
		if (orderId.equals("0")){
			return;
		}

		selectedOrder = appData.getUserOrderById(userorder, orderId);
		if (selectedOrder != null){
			break;
		}
		else{
			System.out.println("OrderID not found!. Please reenter. ");
		}
	}

	print_order_details(selectedOrder);

}

public void print_order_details(Order selectedOrder)
{
	System.out.println("Order ID: " + selectedOrder.getOrderID());
	String [] header = {"Item","Quantity","Price"};
	tablePrinter.printTable(selectedOrder.getItemList(),header);
}

}
