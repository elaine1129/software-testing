package st.assignment;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(JUnitParamsRunner.class)
public class OrderApplicationGetDeliveryRate {


    private Object[] deliveryParam() {
            return new Object[]{
                    new Object[]{
                            "Bukit Beruang",
                            Arrays.asList(
                                    new DeliveryRates("Bukit Beruang", 1),
                                    new DeliveryRates("Bemban", 2)
                            ),
                            1
                    },
                    new Object[]{
                            "Not within melaka",
                            Arrays.asList(
                                    new DeliveryRates("Bukit Beruang", 1),
                                    new DeliveryRates("Bemban", 2)
                            ),
                            0
                    }
            };
        }


    @Test
    @Parameters( method = "deliveryParam")
    public void testGetDeliveryRates(String userArea, List<DeliveryRates> mockList,double expectedRates){
        User mockUser = mock(User.class);
        Address mockAddress = mock(Address.class);
        ApplicationData mockAppData = mock(ApplicationData.class);

        when(mockUser.getUserAddress()).thenReturn(mockAddress);
        when(mockAddress.getArea()).thenReturn(userArea);
        when(mockAppData.getDeliveryRates()).thenReturn(mockList);

        OrderApplication testapp = new OrderApplication(mock(MyScanner.class), mockAppData);

        assertEquals(expectedRates, testapp.get_delivery_rate(mockUser), 0);
    }

}