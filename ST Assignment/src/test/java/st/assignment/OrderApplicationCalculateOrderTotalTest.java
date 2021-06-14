package st.assignment;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class OrderApplicationCalculateOrderTotalTest {

    private Guest aGuest = new Guest();
    private Member aMember = new Member();

    private Item promoItem11 = new Item(135,140,true);
    private Item nonPromoItem8 = new Item(70,75,false);

    private Object[] orderParam() {
            return new Object[]{
                    new Object[]{
                            Arrays.asList(new OrderDetails(promoItem11, 1)), aMember, 128.25
                    },
                    new Object[]{
                            Arrays.asList(new OrderDetails(nonPromoItem8, 1)), aMember, 70
                    },
                    new Object[]{
                            Arrays.asList(new OrderDetails(nonPromoItem8, 1), new OrderDetails(promoItem11, 1)), aMember, 198.25
                    },
                    new Object[]{
                            Arrays.asList(new OrderDetails(promoItem11, 1)), aGuest, 133
                    },
                    new Object[]{
                            Arrays.asList(new OrderDetails(nonPromoItem8, 1)), aGuest, 75
                    },
                    new Object[]{
                            Arrays.asList(new OrderDetails(promoItem11, 1), new OrderDetails(promoItem11, 1)), aGuest, 266
                    }
            };
        }


    @Test
    @Parameters( method = "orderParam")
    public void testCalculateOrderTotal(List<OrderDetails> orderDetailsList, User user, double expectedTotal){
        Order mockOrder = mock(Order.class);
        when(mockOrder.getItemList()).thenReturn(orderDetailsList);
        OrderApplication testapp = new OrderApplication();

        assertEquals(expectedTotal, testapp.calculate_order_total(mockOrder, user), 0);
    }
}