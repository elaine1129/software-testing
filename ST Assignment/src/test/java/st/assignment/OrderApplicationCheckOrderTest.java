package st.assignment;

import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import st.assignment.lib.TablePrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class
OrderApplicationCheckOrderTest {

    private List<Order> orderList = new ArrayList<>();

    @Test
    public void testCheckOrder(){
        MyScanner mockScanner = mock(MyScanner.class);
        ApplicationData mockData = mock(ApplicationData.class);
        OrderApplication orderApp = new OrderApplication(mockScanner, mockData);
        OrderApplication appSpy = spy(orderApp);
        Member dummyUser = new Member().setMemberID("1");

        OrderDetails od1 = new OrderDetails(new Item(10,20,false),1);
        OrderDetails od2 = new OrderDetails(new Item(20,10,false),2);

        orderList.add(new Order().setOrderID("O0001").setTotalPrice(50).setOrderStatus("Paid").setPayment(new Payment("Online Banking")).setOrderUser(dummyUser).setItemlist(Arrays.asList(od1,od2)));

        when(mockData.getOrderArray()).thenReturn(orderList);

        //enter order id to select order
        //test a order id that does not exist
        when(mockScanner.nextLine()).thenReturn("No such id");
        //reenter order id, enter order id that exist
        when(mockScanner.nextLine()).thenReturn("O0001");
        when(mockData.getUserOrderById(anyListOf(Order.class), eq("O0001"))).thenReturn(orderList.get(0));

        doNothing().when(appSpy).print_order_details(anyObject());

        appSpy.check_order_status(dummyUser);

        ArgumentCaptor<Order> actualOrder = ArgumentCaptor.forClass(Order.class);
        verify(appSpy).print_order_details(actualOrder.capture());
        assertEquals(orderList.get(0), actualOrder.getValue());
    }

    @Test
    public void testCheckOrderIfNoOrderMade(){
        MyScanner mockScanner = mock(MyScanner.class);
        ApplicationData mockData = mock(ApplicationData.class);
        TablePrinter mockTp = mock(TablePrinter.class);
        OrderApplication orderApp = new OrderApplication(mockScanner, mockData).setTablePrinter(mockTp);
        OrderApplication appSpy = spy(orderApp);
        Member dummyUser = new Member().setMemberID("1");

        //return an empty list
        when(mockData.getOrderArray()).thenReturn(orderList);

        //return enter to continue then back to main menu
        when(mockScanner.nextLine()).thenReturn("\r");
        appSpy.check_order_status(dummyUser);

        //verify that print table and print order details is never invoked
        verify(mockTp, never()).printTable(anyList(), anyObject());
        verify(appSpy, never()).print_order_details(anyObject());
    }
}