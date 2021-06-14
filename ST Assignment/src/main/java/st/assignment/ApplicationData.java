package st.assignment;

import java.util.ArrayList;
import java.util.List;


public class ApplicationData{

    private CSVUtil csvUtil = new CSVUtil();

    private List<Order> orderArray;
    private List<User> userArray;
    private List<Item> itemArray;
    private List<DeliveryRates> deliveryRates;
    private List<OrderDetails> orderdetailslist;

    private User loggedInUser;

    public static final String ITEM_FILE = "itemList.csv";
    public static final String MEMBER_FILE = "memberList.csv";
    public static final String DELIVERY_RATES_FILE = "deliveryRates.csv";
    public static final String ORDER_DETAILS_LIST_FILE = "orderDetailsList.csv";
    public static final String ORDER_LIST_FILE = "orderList.csv";
    public static final String GUEST_DELIVERY = "guestDelivery.csv";

    public User getLoggedInUser (){
        return loggedInUser;
    }

    public Member getLoggedInMember (){
        return (Member) loggedInUser;
    }

    public void setLoggedInUser (User u){
        this.loggedInUser = u;
    }

    
    public List<Order> getOrderArray (){
        return orderArray;
    }

    public List<Item> getItemArray (){
        return itemArray;
    }

    public List<User> getUserArray (){
        return userArray;
    }


    public Order getUserOrderById (List<Order> userOrder ,String id){
        for (Order order : userOrder) {
            if (order.getOrderID().equals(id))
                return order;
        }
        return null;
    }

    public Item getItemById(int id){
        for (Item item : itemArray) {
            if(item.getItemId() == id)
                return item;
        }
        return null;
    }

    public void addOrder(Order newOrder){
        orderArray.add(newOrder);
    }


    public Item getItemByName(String name){
        for (Item item : itemArray) {
            if(item.getItemName().equals(name)){
                return item;
            }
        }
        return null;
    }

    public String newMemberID() {
        return String.format("%s%03d", "M", userArray.size()+1);
    }

    public User getMemberById (String id)
    {
        for (User user : userArray) {
            if(user instanceof Member){
                if(((Member) user).getMemberID().equals(id))
                    return user;
            }
        }
        return null;
    }


    public User[] getUserArr () {
		User [] userArr = new User [userArray.size()];
		userArr = userArray.toArray(userArr);
		return userArr;		
	}

    public List<DeliveryRates> getDeliveryRates (){
        return deliveryRates;
    }

    public DeliveryRates[] getDeliveryRatesArr (){
        return deliveryRates.toArray(DeliveryRates[]::new);
    }

    // ADD new signup user to csv
    public void addNewMember(User newMember){
        if(!(newMember.getUserAddress().getState().equalsIgnoreCase("melaka"))){
            throw new IllegalArgumentException("Only Melaka State can be accepted");
        }

        //add new member to array
        userArray.add(newMember);

        List<Member> memberArrayList = new ArrayList<>();
        for (User user : userArray) {
            if (user instanceof Member) {
                memberArrayList.add((Member) user);
            }
        }

        csvUtil.writeWritableToCSV(memberArrayList, MEMBER_FILE);
    }

    //TO WRITE TO ORDER FILES
    public void updateOrderToFile(){
        if(getLoggedInUser() instanceof Guest){
            //record guest records
            List<Order> guestOrder = new ArrayList<>();

            for (Order order : orderArray) {
                if(order.getOrderUser() instanceof Guest){
                    guestOrder.add(order);
                }
            }

            List<String> guestRecord = new ArrayList<>();

            for (Order order : guestOrder) {
                if(order.getOrderUser().getName()!= null){
                    String row = String.format("%s,%s,%s", order.getOrderID(), order.getOrderUser().getName(),order.getOrderUser().getUserAddress().toFile());
                    guestRecord.add(row);
                }
            }
            if(guestRecord.size()>0){
                csvUtil.appendCSV(guestRecord, GUEST_DELIVERY);
            }
        }


        if(orderArray.size()>0){
            csvUtil.writeWritableToCSV(orderArray, ORDER_LIST_FILE);

            List<String> orderDetailsData = new ArrayList<String>();
            for (Order order : orderArray) {
                String singleRow;

                for (OrderDetails orderDetails : order.getItemList()) {
                    singleRow = String.format("%s,%s", order.getOrderID(),orderDetails.toFile());
                    orderDetailsData.add(singleRow);
                }
            }
            csvUtil.writeCSV(orderDetailsData, ORDER_DETAILS_LIST_FILE);
        }
    }



// read files and initialize array --
    private void readOrderDetailsFile(){
        String[][] orderDetailsData = csvUtil.readCSV(ORDER_DETAILS_LIST_FILE);

            for (String[] data : orderDetailsData) {
                //skip first line
                if (data == orderDetailsData[0]) continue;
                if(data[0].equals("")) break;

                String orderID = data[0];
                String ODId = data[1];
                String ItemName = data[2];
                Item itemTemp = getItemByName(ItemName);

                int qty = Integer.parseInt(data[3]);
                double price = Double.parseDouble(data[4]);

                orderdetailslist.add(new OrderDetails(orderID, ODId, itemTemp,qty,price));
            }
    }



    private void readOrderFile(){
        String[][] orderData = csvUtil.readCSV(ORDER_LIST_FILE);

        if(orderData == null){
            return;
        }

            for (String[] data : orderData) {
                //skip first line
                if (data == orderData[0]) continue;

                if(data[0].equals("")) break;

                String orderId = data[0];
                String userId = data[1];
                double totalPrice = Double.parseDouble(data[2]);
                String orderStatus = data[3];
                String paymentMethod = data[4];

                ArrayList<OrderDetails> temp = new ArrayList<>();

                for (OrderDetails orderDetails : orderdetailslist) {
                    if (orderDetails.getOrderID().equals(orderId)) {
                        temp.add(orderDetails);
                    }
                }
                Order tempOrder;
                if (userId.equals("GUEST")) {
                    tempOrder = new Order(new Guest(),
                            orderId,
                            totalPrice,
                            orderStatus,
                            temp,
                            new Payment(paymentMethod)
                    );
                }

                //if is member then link order with member
                else {
                    tempOrder = new Order(getMemberById(userId),
                            orderId,
                            totalPrice,
                            orderStatus,
                            temp,
                            new Payment(paymentMethod)
                    );
                }

                orderArray.add(tempOrder);
            }

    }


    private void readDeliveryRatesFile(){
        String[][] deliveryRatesData = csvUtil.readCSV(DELIVERY_RATES_FILE);

        for (String[] data : deliveryRatesData) {
            //skip first line
            if (data == deliveryRatesData[0]) continue;
            if(data[0].equals("")) break;

            String area = data[0];
            double rates = Double.parseDouble(data[1]);

            DeliveryRates temp = new DeliveryRates(area, rates);

            deliveryRates.add(temp);
        }
    }


    private void readMemberFile(){
        String[][] memberData = csvUtil.readCSV(MEMBER_FILE);

        for (String[] data : memberData) {
                //skip first line
                if (data == memberData[0]) continue;

                if(data[0].equals("")) break;

                String memberId = data[0];
                String name = data[1];
                String password = data[2];
                String phone = data[3];

                String unitno = data[4];
                String street = data[5];
                String postalcode = data[6];
                String area = data[7];
                String district = data[8];
                String state = data[9];


                Address tempAd = new Address(unitno, street, postalcode, area, district, state);
                Member tempMem = new Member(memberId, name, password, phone, tempAd);

                userArray.add(tempMem);
            }
    }



    private void readItemFile(){
        String[][] itemData = null;

        itemData = csvUtil.readCSV(ITEM_FILE);

        for (String[] data : itemData) {
            //skip first line
            if (data == itemData[0]) continue;
            if(data[0].equals("")) break;

            int id = Integer.parseInt(data[0]);
            String name = data[1];
            String type = data[2];
            double memberPrice = Double.parseDouble(data[3]);
            double nonMemberPrice = Double.parseDouble(data[4]);
            String promotionText = data[5];
            boolean promotion;
            promotion = promotionText.equalsIgnoreCase("Yes");
            Item temp = new Item (id, name,type, memberPrice, nonMemberPrice, promotion);
            itemArray.add(temp);
        }
    }

    public ApplicationData setCsvUtil(CSVUtil csvUtil) {
        this.csvUtil = csvUtil;
        return this;
    }

    public void readDataFromFile(){
        readDeliveryRatesFile();
        readMemberFile();
        readItemFile();
        readOrderDetailsFile();
        readOrderFile();
    }

    public void setDeliveryRate(List<DeliveryRates> deliveryRates){
        this.deliveryRates = deliveryRates;
    }

    public ApplicationData(){
        deliveryRates = new ArrayList<DeliveryRates>();
        itemArray = new ArrayList<Item>();
        userArray = new ArrayList<User>();
        orderArray = new ArrayList<Order>();
        orderdetailslist = new ArrayList<OrderDetails>();
    }
}