package st.assignment;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class
OrderApplicationMakePaymentTest {

    private Object[] makePaymentParam() {
            return new Object[]{
                    //valid payment method
                    new Object[]{
                            new Payment().setPaymentMethod("Online Banking").setTotalPrice(137.50),
                            "A",
                            137.50
                    },
            };
        }

    @Test
    @Parameters ( method = "makePaymentParam")
    public void testMakePayment(Payment expectedPayment, String paymentMethod, String totalPrice){
        Order mockOrder = mock(Order.class);
        MyScanner mockScanner = mock(MyScanner.class);
        ApplicationData mockData = mock(ApplicationData.class);
        OrderApplication testPayment = new OrderApplication(mockScanner, mockData);

        when(mockOrder.getTotalPrice()).thenReturn(Double.valueOf(totalPrice));

        when(mockScanner.nextLine()).thenReturn(paymentMethod);

        //call method
        Payment actualPayment = testPayment.make_payment(mockOrder);

        verify(mockOrder).setOrderStatus("Paid & Ready for Delivery");
        verify(mockOrder).setPayment(anyObject());

        assertEquals(expectedPayment.getPaymentMethod(), actualPayment.getPaymentMethod());
        assertEquals(expectedPayment.getTotalPrice(), actualPayment.getTotalPrice(), 0);
    }

    @Test
    public void testInvalidMakePayment(){
        Order mockOrder = mock(Order.class);
        MyScanner mockScanner = mock(MyScanner.class);
        ApplicationData mockData = mock(ApplicationData.class);
        OrderApplication testPayment = new OrderApplication(mockScanner, mockData);

        when(mockOrder.getTotalPrice()).thenReturn(137.50);

        //first input is invalid payment method, second select online banking as payment method
        when(mockScanner.nextLine()).thenReturn("C").thenReturn("A");

        //call method
        Payment actualPayment = testPayment.make_payment(mockOrder);

        verify(mockOrder).setOrderStatus("Paid & Ready for Delivery");
        verify(mockOrder).setPayment(anyObject());

        assertEquals("Online Banking", actualPayment.getPaymentMethod());
        assertEquals(137.50, actualPayment.getTotalPrice(), 0);
    }
}