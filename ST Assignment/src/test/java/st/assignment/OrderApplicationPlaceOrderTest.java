package st.assignment;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class
OrderApplicationPlaceOrderTest {
    private Member orderUser;

    private List<Item> itemList;

    List<Order> dummyOrderList;

    OrderApplication testapp;
    OrderApplication appSpy;

    MyScanner mockScanner;
    MyScanner spyScanner;
    ApplicationData mockAppData;

    @Before
    public void setUp(){
        orderUser = new Member();
        itemList = new ArrayList<>();
        MyScanner myScanner = new MyScanner();
        spyScanner = spy(myScanner);
        dummyOrderList = mock(ArrayList.class);

        mockScanner = mock(MyScanner.class);
        mockAppData = mock(ApplicationData.class);

        testapp = new OrderApplication(mockScanner, mockAppData);
        appSpy = spy(testapp);
    }


    @Test
    @Parameters ({"2, 32.5", "1, 20.5"})
    public void testPlaceOrder(int quantity, double expectedTotalPrice){
        itemList.add(new Item(15, 15.5, false));


        when(dummyOrderList.size()).thenReturn(1);

        List<OrderDetails> expectedOD = new ArrayList<>();
        expectedOD.add(new OrderDetails(itemList.get(0), quantity));

        Order expectedOrder = new Order(orderUser, expectedOD, "O0001");

        expectedOrder.setTotalPrice(expectedTotalPrice);

        testapp = new OrderApplication(mockScanner, mockAppData);
        appSpy = spy(testapp);
        doNothing().when(appSpy).view_item_menu(anyObject());

        //set order id
        when(mockAppData.getOrderArray()).thenReturn(dummyOrderList);


        //enter item id
        when(mockScanner.nextInt()).thenReturn(1);

        //select item
        when(mockAppData.getItemById(anyInt())).thenReturn(itemList.get(0));

        //select 1 quantity
        when(mockScanner.nextQuantity()).thenReturn(quantity);
        doReturn(itemList.get(0).getMemberPrice() * quantity).when(appSpy).calculate_order_total(anyObject(), anyObject());

        //select no more item
        when(mockScanner.next()).thenReturn("N");

        //return delivery rate
        doReturn(2.5).when(appSpy).get_delivery_rate(anyObject());

        doReturn(new Payment("A")).when(appSpy).make_payment(anyObject());

        appSpy.place_order(itemList, orderUser);
        ArgumentCaptor<Order> actualOrder = ArgumentCaptor.forClass(Order.class);

        verify(mockAppData).addOrder(actualOrder.capture());

        assertEquals(expectedOrder.getOrderID(), actualOrder.getValue().getOrderID());
        assertEquals(expectedOrder.getTotalPrice(), actualOrder.getValue().getTotalPrice(), 0);
    }
}