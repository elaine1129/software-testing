package st.assignment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ApplicationDataTest {

    private String[][] dummyDeliveryRatesData = {
            {
                    "This lines is skipped"
            },
            {
                    "Bukit Beruang","10.0"
            },
            {
                    "Jasin", "20.0"
            }
    };

    private String[][] dummyMemberData = {
            {
                    "This lines is skipped"
            },
            {
                    "M001","Tan","password","01234567","No 1","Jln 2/1","50125","Bukit Beruang","District","Melaka"
            }
    };

    private String[][] dummyItemData = {
            {
                    "This lines is skipped"
            },
            {
                    "1","item name","type","10.0","15.0","No"
            }
    };

    private String[][] dummyOrderDetailsData = {
            {
                    "This lines is skipped"
            },
            {
                    "O0000","OD0001","item name","2","20.0"
            },
            {
                    "O0001","OD0001","item name","1","15.0"
            }
    };

    private String[][] dummyOrderData = {
            {
                    "This lines is skipped"
            },
            {
                    "O0000","M001","30.0","Paid & Ready for Delivery","Online Banking"
            },
            {
                    "O0001","GUEST","35.0","Paid & Ready for Delivery","Credit Card"
            }
    };


    private List<DeliveryRates> expectedDeliveryRates;
    private List<Item> expectedItem;
    private List<User> expectedUser;
    private List<OrderDetails> expectedOrderDetails;
    private List<Order> expectedOrder;

    private ApplicationData testAppData;

    private CSVUtil mockCSV;

    @Before
    public void setUp(){
        expectedDeliveryRates = new ArrayList<>();
        expectedItem = new ArrayList<>();
        expectedUser = new ArrayList<>();
        expectedOrderDetails = new ArrayList<>();
        expectedOrder = new ArrayList<>();


        expectedDeliveryRates.add(new DeliveryRates("Bukit Beruang",10.0));
        expectedDeliveryRates.add(new DeliveryRates( "Jasin", 20.0));

        expectedItem.add(new Item(1,"item name","type",10.0,15.0,false));

        expectedUser.add(new Member( "M001","Tan","password","01234567", new Address("No 1","Jln 2/1","50125","Bukit Beruang","District","Melaka")));

        expectedOrderDetails.add(new OrderDetails("O0000","OD0001", expectedItem.get(0),2,20.0));
        expectedOrderDetails.add(new OrderDetails("O0001","OD0001", expectedItem.get(0),1,15.0));

        expectedOrder.add(new Order(expectedUser.get(0),"O0000",30.0,"Paid & Ready for Delivery", Collections.singletonList(expectedOrderDetails.get(0)), new Payment("Online Banking")));
        expectedOrder.add(new Order(new Guest("Guest", new Address("1", "Jln 2", "123541", "Jasin", "124561", "Melaka")),
                "O0001",35.0,"Paid & Ready for Delivery", Collections.singletonList(expectedOrderDetails.get(1)), new Payment("Credit Card")));


        mockCSV = mock(CSVUtil.class);

        //read delivery rates
        when(mockCSV.readCSV(ApplicationData.DELIVERY_RATES_FILE)).thenReturn(dummyDeliveryRatesData);
        when(mockCSV.readCSV(ApplicationData.MEMBER_FILE)).thenReturn(dummyMemberData);
        when(mockCSV.readCSV(ApplicationData.ITEM_FILE)).thenReturn(dummyItemData);
        when(mockCSV.readCSV(ApplicationData.ORDER_DETAILS_LIST_FILE)).thenReturn(dummyOrderDetailsData);
        when(mockCSV.readCSV(ApplicationData.ORDER_LIST_FILE)).thenReturn(dummyOrderData);

        testAppData = new ApplicationData().setCsvUtil(mockCSV);
        testAppData.readDataFromFile();
    }

    @Test
    public void testReadData(){

        //test all data readed
        assertArrayEquals(expectedDeliveryRates.toArray() ,testAppData.getDeliveryRatesArr());
        assertArrayEquals(expectedUser.toArray() ,testAppData.getUserArr());
        assertArrayEquals(expectedItem.toArray() ,testAppData.getItemArray().toArray());
        assertArrayEquals(expectedOrder.toArray() ,testAppData.getOrderArray().toArray());
    }

    @Test
    public void testGetUserOrderById(){
        Order actualorder = testAppData.getUserOrderById(expectedOrder, "O0000");

        assertEquals(expectedOrder.get(0) , actualorder);
        assertNull(testAppData.getUserOrderById(expectedOrder, "A id that does not exist"));
    }

    @Test
    public void testGetItemById(){
        Item actualItem = testAppData.getItemById(1);
        assertEquals(expectedItem.get(0) , actualItem);
        assertNull(testAppData.getItemById(100));
    }


    @Test
    public void testAddOrder(){
        Order newOrder = new Order(expectedUser.get(0), "O0003", 20, "Paid & Ready for Delivery",
                Collections.singletonList(new OrderDetails("O0003", "OD0001", expectedItem.get(0), 2, 20)),
                new Payment("Online Banking"));

        testAppData.addOrder(newOrder);
        assertEquals(newOrder, testAppData.getOrderArray().get(testAppData.getOrderArray().size()-1));
    }

    @Test
    public void testGetItemByName(){
        Item actualItem = testAppData.getItemByName("item name");
        assertEquals(expectedItem.get(0) , actualItem);
        assertNull(testAppData.getItemByName("Item name that does not exist"));
    }

    @Test
    public void testNewMemberId(){
        String actualNewMemberID = testAppData.newMemberID();
        assertEquals("M002" , actualNewMemberID);
    }

    @Test
    public void testGetMemberById(){
        User actualMember = testAppData.getMemberById("M001");
        assertEquals(expectedUser.get(0) , actualMember);
        assertNull(testAppData.getMemberById("ID that does not exist"));
    }

    @Test
    public void testAddNewMember(){
        Member memberToBeAdd = new Member("M002", "Member name", "password", "0111111", new Address("1","Jln 1", "24156", "Jasin","district", "Melaka"));
        expectedUser.add(memberToBeAdd);

        ArgumentCaptor<List> memberListToBeWritten = ArgumentCaptor.forClass(List.class);

        testAppData.addNewMember(memberToBeAdd);
        verify(mockCSV).writeWritableToCSV(memberListToBeWritten.capture(), eq(ApplicationData.MEMBER_FILE));

        assertEquals(memberToBeAdd, testAppData.getUserArray().get(testAppData.getUserArr().length - 1));
        assertEquals(expectedUser, memberListToBeWritten.getValue());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testAddInvalidMember(){
        Member memberToBeAdd = new Member("M002", "Member name", "password", "0111111",
                new Address("1","Jln 1", "24156", "Jasin","district", "Not Melaka"));
        testAppData.addNewMember(memberToBeAdd);
    }


    // test for update order file START---
    @Test
    //test to write a new guest record
    //Guest delivery will only be append to the file when new Guest make a new order
    public void testUpdateGuestDelivery(){

        // set logged user is guest
        testAppData.setLoggedInUser(new Guest("Guest", new Address("1", "Jln 2", "123541", "Jasin", "124561", "Melaka")));

        Order newGuestOrder = new Order(testAppData.getLoggedInUser(), "O0003", 20, "Paid & Ready for Delivery",
                Collections.singletonList(new OrderDetails("O0003", "OD0001", expectedItem.get(0), 2, 20)),
                new Payment("Online Banking"));

        testAppData.addOrder(newGuestOrder);
        testAppData.updateOrderToFile();

        ArgumentCaptor<List> guestRecordToBeWritten = ArgumentCaptor.forClass(List.class);

        verify(mockCSV).appendCSV(guestRecordToBeWritten.capture(), eq(ApplicationData.GUEST_DELIVERY));

        List<String> expectedGuestDeliveryToBeWritten = Stream.of(newGuestOrder)
                .filter(o -> o.getOrderUser() instanceof Guest)
                .map(order -> String.format("%s,%s,%s", order.getOrderID(), order.getOrderUser().getName(), order.getOrderUser().getUserAddress().toFile()))
                .collect(Collectors.toList());


        assertArrayEquals(expectedGuestDeliveryToBeWritten.toArray(), guestRecordToBeWritten.getValue().toArray());
    }

    @Test
    //test if no new guest record, guest delivery file is not updated
    public void testGuestDelivery(){
        testAppData.updateOrderToFile();
        verify(mockCSV, never()).appendCSV(anyList(), eq(ApplicationData.GUEST_DELIVERY));
    }

    @Test
    public void testUpdateOrder(){
        testAppData.updateOrderToFile();
        ArgumentCaptor<List> orderToBeWritten = ArgumentCaptor.forClass(List.class);

        verify(mockCSV).writeWritableToCSV(orderToBeWritten.capture(), eq(ApplicationData.ORDER_LIST_FILE));

        assertArrayEquals(expectedOrder.toArray() ,orderToBeWritten.getValue().toArray());


        ArgumentCaptor<List> orderDetailsToBeWritten = ArgumentCaptor.forClass(List.class);
        verify(mockCSV).writeCSV(orderDetailsToBeWritten.capture(), eq(ApplicationData.ORDER_DETAILS_LIST_FILE));

        List<String> expectedOrderDetailsToBeWritten = expectedOrderDetails.stream()
                .map(orderDetails ->  String.format("%s,%s", orderDetails.getOrderID(), orderDetails.toFile()))
                .collect(Collectors.toList());

        assertArrayEquals(expectedOrderDetailsToBeWritten.toArray() ,orderDetailsToBeWritten.getValue().toArray());
    }

    @Test
    public void testUpdateOrderIfOrderArrayIsEmpty(){
        CSVUtil mockCSV2 = mock(CSVUtil.class);

        ApplicationData testEmptyData = new ApplicationData();
        testEmptyData.setCsvUtil(mockCSV2);
        verify(mockCSV2, never()).writeWritableToCSV(anyList(), eq(ApplicationData.ORDER_LIST_FILE));
        verify(mockCSV2, never()).writeCSV(anyList(), eq(ApplicationData.ORDER_DETAILS_LIST_FILE));
    }
//test add order END---

}